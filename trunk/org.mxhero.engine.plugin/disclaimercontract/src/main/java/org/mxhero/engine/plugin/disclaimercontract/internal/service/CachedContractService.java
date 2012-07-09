package org.mxhero.engine.plugin.disclaimercontract.internal.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.fsqueues.internal.FSQueueService;
import org.mxhero.engine.plugin.disclaimercontract.entity.Contract;
import org.mxhero.engine.plugin.disclaimercontract.entity.Request;
import org.mxhero.engine.plugin.disclaimercontract.internal.repository.CachedContractRepository;
import org.mxhero.engine.plugin.disclaimercontract.internal.repository.jdbc.JdbcContractRepository;
import org.mxhero.engine.plugin.disclaimercontract.service.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class CachedContractService implements ContractService{

	private static Logger log = LoggerFactory.getLogger(CachedContractService.class);
	
	private static final String TMP_FILE_SUFFIX = ".eml";
	private CachedContractRepository cachedRepository;
	private JdbcContractRepository repository;
	private File directory;
	
	@Autowired(required=true)
	public CachedContractService(CachedContractRepository cachedRepository,
			JdbcContractRepository repository) {
		super();
		this.cachedRepository = cachedRepository;
		this.repository = repository;
	}

	@Override
	public boolean isApproved(Long ruleId, String recipient) {
		log.debug("isApproved for ruleId="+ruleId+" recipient="+recipient);
		boolean result = cachedRepository.containsfindByRuleAndRecipient(ruleId, recipient);
		log.debug("isApproved for ruleId="+ruleId+" recipient="+recipient+" result="+result);
		return result;
	}

	@Transactional(readOnly=false,propagation=Propagation.REQUIRES_NEW)
	@Override
	public Request request(Request request, MimeMail mail) {

		request.setMessageId(mail.getMessageId());
		request.setPending(false);
		request.setRequestDate(Calendar.getInstance());
		request.setPhase(mail.getPhase().name());

		log.debug("requesting "+request.toString());
		
		File tmpFile=null;
		OutputStream os=null;
		try{
			try{
				mail.getMessage().removeHeader(FSQueueService.SENDER_HEADER);
				mail.getMessage().removeHeader(FSQueueService.RECIPIENT_HEADER);
				mail.getMessage().removeHeader(FSQueueService.OUTPUT_SERVICE_HEADER);
				mail.getMessage().removeHeader(FSQueueService.FORCED_PRIORITY_HEADER);
				mail.getMessage().addHeader(FSQueueService.SENDER_HEADER, mail.getSender());
				mail.getMessage().addHeader(FSQueueService.RECIPIENT_HEADER, request.getRecipient());
				mail.getMessage().addHeader(FSQueueService.OUTPUT_SERVICE_HEADER, mail.getResponseServiceId());
				if(request.getRulePriority()!=null){
					mail.getMessage().addHeader(FSQueueService.FORCED_PRIORITY_HEADER, Integer.toString((request.getRulePriority()+200)));
				}
				mail.getMessage().saveChanges();
				tmpFile = File.createTempFile(request.getRecipient()+"_"+request.getSenderDomain()+"_"+request.getRuleId(), TMP_FILE_SUFFIX,directory);
				log.debug("creating file="+tmpFile.getAbsolutePath());
				os = new FileOutputStream(tmpFile);
				mail.getMessage().writeTo(os);
				request.setPath(tmpFile.getAbsolutePath());
			}catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}finally{
				if(os!=null){
					try{os.flush();os.close();}catch(Exception e){}
				}
			}
			log.debug("created file="+request.getPath());
		
			request = repository.addRequest(request);
		}catch(Exception e){
			try{tmpFile.delete();}catch(Exception e2){}
			log.warn("deleted file file="+request.getPath(),e);
			throw new RuntimeException(e.getMessage());
		}
		return request;
	}

	public String getDirectory() {
		return directory.getAbsolutePath();
	}

	public void setDirectory(String directory) throws IOException {
		if(directory==null || directory.trim().length()<1){
			throw new IllegalArgumentException("not valid loadPath:"+directory);
		}
		this.directory = new File(directory);
		this.directory.mkdirs();
		
		if(!this.directory.exists()){
			throw new IllegalArgumentException("clould not create loadPath:"+directory);
		}
		
	}

	@Transactional(readOnly=false,propagation=Propagation.REQUIRES_NEW)
	@Override
	public Contract sign(Request request) {
		Contract contract = null;
		contract = repository.findByRuleAndRecipient(request.getRuleId(), request.getRecipient());
		if(contract == null){
			contract = new Contract();
			contract.setAditionalData(request.getAdditionalData());
			contract.setApprovedDate(request.getApprovedDate());
			contract.setDisclaimerHtml(request.getDisclaimerHtml());
			contract.setDisclaimerPlain(request.getDisclaimerPlain());
			contract.setRecipient(request.getRecipient());
			contract.setSenderDomain(request.getSenderDomain());
			contract.setRuleId(request.getRuleId());
			contract = repository.create(contract);
		}
		repository.markDone(request.getId());
		cachedRepository.addContract(contract);
		return contract;
	}
}

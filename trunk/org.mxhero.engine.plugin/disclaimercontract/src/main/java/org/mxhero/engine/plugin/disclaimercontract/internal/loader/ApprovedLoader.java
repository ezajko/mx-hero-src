package org.mxhero.engine.plugin.disclaimercontract.internal.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.fsqueues.internal.FSQueueService;
import org.mxhero.engine.plugin.disclaimercontract.entity.Request;
import org.mxhero.engine.plugin.disclaimercontract.internal.repository.ContractRepository;
import org.mxhero.engine.plugin.disclaimercontract.service.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class ApprovedLoader implements Runnable{
	private static Logger log = LoggerFactory.getLogger(ApprovedLoader.class);
	
	private static final String RECIPIENT_KEY = "mxrecipient";
	private static final String SUBJECT_KEY = "subject";
	private static final String DATE_KEY = "date";
	private static final long CHECK_TIME = 1000;
	private Thread thread;
	private boolean keepWorking = false;
	private Long updateTime = 30000l;
	private Integer hours = 24;
	private String loadDirectory;
	@Autowired(required=true)
	private ContractRepository repository;
	@Autowired(required=true)
	private ContractService service;
	@Autowired(required=true)
	private DisclaimerContractTemplate template;
	
	public void start(){
		thread=new Thread(this);
		keepWorking=true;
		thread.start();
	}

	public void stop(){
		keepWorking=false;	
		if(thread!=null){
			try {
				thread.join();
			} catch (InterruptedException e) {}
		}
	}

	@Override
	public void run() {
		long lastUpdate = System.currentTimeMillis();
		log.debug("started work");
		while(keepWorking){
			try {
				Thread.sleep(CHECK_TIME);
			} catch (InterruptedException e) {}
			if(lastUpdate+updateTime-System.currentTimeMillis()<0){
				List<Request> requests = repository.pending();
				log.debug("found to load "+requests.size()+" pending request");
				for(Request request : requests){
					try{
						loadFile(request);
					}catch (Exception e){
						log.error("error loading request "+request.getId(),e);
					}
				}
				List<Request> oldRequests = repository.oldNotAccepted(hours);
				log.debug("found to remove "+oldRequests.size()+" request");
				for(Request request : oldRequests){
					try{
						removeOld(request);
					}catch (Exception e){
						log.error("error removing request "+request.getId(),e);
					}
				}
				lastUpdate=System.currentTimeMillis();
			}
		}
	}

	@Transactional(readOnly=false)
	private void loadFile(Request request){
		File loadDirectoryFile = null;
		File fromFile = null;
		File toFile = null;
		try{
			String phase = (Mail.Phase.valueOf(request.getPhase())==null)?Mail.Phase.send.name():Mail.Phase.valueOf(request.getPhase()).name();
			loadDirectoryFile = new File(loadDirectory,phase);
			fromFile = new File(request.getPath());
			toFile = new File(loadDirectoryFile,fromFile.getName());
			if(loadDirectoryFile.exists() && loadDirectoryFile.isDirectory()){
				Files.copy(fromFile, toFile);
				if(Request.CONTRACT_TYPE.equalsIgnoreCase(request.getType())){
					service.sign(request);
				}else if(Request.ONE_TYPE.equalsIgnoreCase(request.getType())){
					repository.markDone(request.getId());
				}else{
					throw new RuntimeException("no valid request type");
				}
				fromFile.delete();
			}else{
				log.error("error loadDirectory do not exists "+loadDirectory+phase);
			}
		}catch(Exception e){
			if (toFile != null && toFile.exists()){
				try{toFile.delete();}catch(Exception ed){log.error("error loading "+request,e);}
			}
			throw new RuntimeException(e);
		}
	}

	@Transactional(readOnly=false)
	private void removeOld(Request request){
		File loadDirectoryFile = null;
		File fromFile = null;
		FileOutputStream noticeMailFile = null;
		FileInputStream is = null;
		try{
			loadDirectoryFile = new File(loadDirectory,Mail.Phase.out.name());
			fromFile = new File(request.getPath());
			is = new FileInputStream(request.getPath());
			MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()),is);
			String from = message.getHeader(FSQueueService.SENDER_HEADER)[0];
			String recipient = message.getHeader(FSQueueService.RECIPIENT_HEADER)[0];
			String subject = message.getSubject();
			String sentDate = message.getHeader("Date")[0];
			String outputService = message.getHeader(FSQueueService.OUTPUT_SERVICE_HEADER)[0];
			MimeMessage replyMessage = (MimeMessage)message.reply(false);
			if(from.contains("@")){
				String systemSender = "noreply@"+from.split("@")[1].trim();
				replyMessage.setSender(new InternetAddress(systemSender,false));
				replyMessage.setReplyTo(new Address[]{new InternetAddress(systemSender,false)});
			}
			String content = replaceTextVars(subject, recipient, template.getTemplate(template.getDefaultLocale()), sentDate);
			replyMessage.setContent(content,"text/html");
			replyMessage.setHeader(FSQueueService.SENDER_HEADER, from);
			replyMessage.setHeader(FSQueueService.RECIPIENT_HEADER, from);
			replyMessage.setHeader(FSQueueService.OUTPUT_SERVICE_HEADER, outputService);
			replyMessage.saveChanges();
			noticeMailFile = new FileOutputStream(new File(loadDirectoryFile,fromFile.getName()));
			replyMessage.writeTo(noticeMailFile);
			noticeMailFile.flush();
		}catch (Exception e){
			log.warn("error while trying to send message",e);
		}finally{
			if(is!=null){
				try{is.close();}catch(Exception e){}
			}
			if(noticeMailFile!=null){
				try{noticeMailFile.close();}catch(Exception e){}
			}
			if(fromFile!=null){
				try{fromFile.delete();}catch(Exception e){}
			}
		}
		repository.remove(request.getId());
	}
	
	private String replaceTextVars(String subject, String recipient,
			String content, String date) {
		String tagregex = "\\$\\{[^\\{]*\\}";
		Pattern p2 = Pattern.compile(tagregex);
		StringBuffer sb = new StringBuffer();
		Matcher m2 = p2.matcher(content);
		int lastIndex = 0;
		log.debug("subject="+subject+" recipient="+recipient+" date="+date+" content="+content);
		while (m2.find()) {
			lastIndex = m2.end();
			String key = content.substring(m2.start() + 2, m2.end() - 1);
			if (key.equalsIgnoreCase(RECIPIENT_KEY)) {
				m2.appendReplacement(sb, recipient);
			} else if (key.equalsIgnoreCase(SUBJECT_KEY)) {
				m2.appendReplacement(sb, subject);
			} else if (key.equalsIgnoreCase(DATE_KEY)) {
				m2.appendReplacement(sb, date);
			}else{
				m2.appendReplacement(sb,"\\${"+key+"}");
			}
		}
		sb.append(content.substring(lastIndex));
		return sb.toString();
	}
	
	public String getLoadDirectory() {
		return loadDirectory;
	}

	public void setLoadDirectory(String loadDirectory)  {
		this.loadDirectory = loadDirectory;
	}

	public ContractRepository getRepository() {
		return repository;
	}

	public void setRepository(ContractRepository repository) {
		this.repository = repository;
	}

	public ContractService getService() {
		return service;
	}

	public void setService(ContractService service) {
		this.service = service;
	}

	public DisclaimerContractTemplate getTemplate() {
		return template;
	}

	public void setTemplate(DisclaimerContractTemplate template) {
		this.template = template;
	}
	
}

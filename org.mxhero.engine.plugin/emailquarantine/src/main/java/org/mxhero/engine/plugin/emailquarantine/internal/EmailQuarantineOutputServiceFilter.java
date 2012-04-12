package org.mxhero.engine.plugin.emailquarantine.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.mxhero.engine.commons.connector.InputService;
import org.mxhero.engine.commons.connector.OutputServiceFilter;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.mail.api.Mail.Phase;
import org.mxhero.engine.plugin.emailquarantine.repository.QuarantineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailQuarantineOutputServiceFilter implements OutputServiceFilter{

	private static Logger log = LoggerFactory.getLogger(EmailQuarantineOutputServiceFilter.class);
	private static final String TMP_FILE_SUFFIX = ".eml";
	private static final String TMP_FILE_PREFIX = "clone";
	private static final int DEFERRED_SIZE = 1*1024*1024;
	private InputService inputService;
	private QuarantineRepository repository;
	
	@Autowired(required=true)
	public EmailQuarantineOutputServiceFilter(QuarantineRepository repository) {
		this.repository = repository;
	}

	public void dofilter(MimeMail mail) {
		if(mail !=null && mail.getStatus().equals(Mail.Status.drop)){
			if(mail.getPhase().equals(Mail.Phase.receive)){
				String domain = (mail.getBussinesObject()!=null)?mail.getBussinesObject().getRecipient().getDomain().getId():mail.getRecipientDomainId();
				String email = getEmail(domain);
				if(email!=null){
					quarantine(mail,email);
				}
			}else if(mail.getPhase().equals(Mail.Phase.send)){
				String domain = (mail.getBussinesObject()!=null)?mail.getBussinesObject().getSender().getDomain().getId():mail.getSenderDomainId();
				String email = getEmail(domain);
				if(email!=null){
					quarantine(mail,email);
				}
			}else{
				log.warn("wrong phase: "+mail.getPhase().toString());
			}
		}
	}

	private String getEmail(String domain){
		return repository.findEmail(domain);
	}
	
	private void quarantine(MimeMail mail, String email){
		if(inputService!=null){
			InputStream is = null;
			OutputStream os = null;
			try {
				mail.getMessage().setHeader("X-mxHero-Quarantine", (mail.getStatusReason()!=null)?mail.getStatusReason():Mail.Status.drop.name());
				mail.getMessage().saveChanges();
				if(mail.getInitialSize()>DEFERRED_SIZE){
					File tmpFile = File.createTempFile(TMP_FILE_PREFIX, TMP_FILE_SUFFIX);
					os = new FileOutputStream(tmpFile);
					mail.getMessage().writeTo(os);
					is = new SharedTmpFileInputStream(tmpFile);
					
				}else{
					os = new ByteArrayOutputStream();
					mail.getMessage().writeTo(os);
					is = new ByteArrayInputStream(((ByteArrayOutputStream)os).toByteArray());
				}
				MimeMail newMimeMail = new MimeMail(mail.getSender(), email, is, mail.getResponseServiceId());
				mail.setPhase(Phase.out);
				mail.setStatus(Mail.Status.deliver);
				inputService.addMail(newMimeMail);
				log.info("QUARANTINE "+newMimeMail);
			}catch(Exception e){
				log.warn("error while creating quarantine email "+mail);
			}
		}else{
			log.warn("input service is out");
		}
	}

	public InputService getInputService() {
		return inputService;
	}

	public void setInputService(InputService inputService) {
		this.inputService = inputService;
	}
	
}

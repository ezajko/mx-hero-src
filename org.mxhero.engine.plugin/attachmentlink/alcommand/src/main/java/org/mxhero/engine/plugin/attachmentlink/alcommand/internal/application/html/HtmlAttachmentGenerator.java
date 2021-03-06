/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.html;

import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.EscapeTool;
import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.AttachmentGenerator;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.LinkManager;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.Message;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.MessageAttachRecipient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HtmlAttachmentGenerator implements AttachmentGenerator {
	
	private static Logger log = Logger.getLogger(HtmlAttachmentGenerator.class);
	
	@Autowired
	private PBEStringEncryptor encryptor;
	@Autowired
	private LinkManager linkManager;
	
	@Value("#{fileAttachNames}")
	private Map<String, String> fileNames;
	
	private VelocityEngine ve;

	@Autowired
	public HtmlAttachmentGenerator(@Value("${template.html.path}")String templatesPath) {
		ve = new VelocityEngine();
		Properties p = new Properties();
		p.setProperty("resource.loader", "file");
		p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		p.setProperty("file.resource.loader.path", templatesPath);
		p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
		try {
			ve.init(p);
		} catch (Exception e) {
			log.error("Error init bean HTML attach. Missconfigured Velocity Engine."+e.getClass().getName()+" - "+e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public BodyPart createNewAttach(Message mail) {
        MimeBodyPart part = null;
        try {
	        String language = mail.getLocale().getLanguage();
	        StringBuffer template = new StringBuffer("attach_");
			template.append(language);
	        template.append(".vm");
			Template t = ve.getTemplate(template.toString(),"UTF-8");
			log.debug("template:"+t.getName());
	        createLinks(mail);
	        Set<MessageAttachRecipient> messageAttachRecipientForRecipient = mail.getMessageAttachRecipient();
	        if(messageAttachRecipientForRecipient.isEmpty())throw new RuntimeException("ERRORRRRRRRRR!!!!!!!!!!!!!!!!!!");
	        VelocityContext context = getContextVelocity(mail, messageAttachRecipientForRecipient);
	        StringWriter writer = new StringWriter();
	        t.merge( context, writer );
        	part = new MimeBodyPart();
        	DataSource ds = new ByteArrayDataSource(writer.toString(), "text/html");
        	String attachFileName = fileNames.get(language);
        	if(attachFileName == null)attachFileName = "attachments.html";
        	part.setDataHandler(new DataHandler(ds));
        	part.setFileName(attachFileName);
        	part.setDisposition(Part.ATTACHMENT);
        	part.setHeader("Content-Type","text/html; charset=\"utf-8\"");
		} catch (Exception e) {
			log.error("Error generating HTML attach. MailId: "+mail.getMessagePlatformId()+" - "+e.getClass().getName()+" - "+e.getMessage());
			throw new RuntimeException("Could not generate HTML attachment for email "+mail.getMessagePlatformId());
		}
        return (BodyPart)part;
	}

	private VelocityContext getContextVelocity(Message mail, Set<MessageAttachRecipient> messageAttachRecipientForRecipient) throws Exception{
		VelocityContext context = new VelocityContext();
		context.put("files", messageAttachRecipientForRecipient);
		context.put("recipientEmail",URLEncoder.encode(new ArrayList<MessageAttachRecipient>(messageAttachRecipientForRecipient).get(0).getRecipient(),"ASCII"));
        context.put("messageId", URLEncoder.encode(encryptor.encrypt(mail.getId().toString()),"ASCII"));
		context.put("esc", new EscapeTool());
		if(mail.hasToProcessRecipient()){
			context.put("urlStorage", mail.getResultCloudStorageRecipient().getBody());
		}
		return context;
	}

	private void createLinks(Message mail) {
		for(MessageAttachRecipient msg : mail.getMessageAttachRecipient()){
			String link = linkManager.createLink(msg);
			msg.getAttach().setTempLink(link);
		}
	}

	public PBEStringEncryptor getEncryptor() {
		return encryptor;
	}

	public void setEncryptor(PBEStringEncryptor encryptor) {
		this.encryptor = encryptor;
	}

}

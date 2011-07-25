package org.mxhero.engine.domain.mail.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LogMail {

	private static Logger log = LoggerFactory.getLogger(LogMail.class);
	
	public static void saveErrorMail(MimeMessage message, String prefix, String suffix, String directory){
		if(message!=null){
			File outFile=null;
			FileOutputStream fos=null;
			try {

				File directoryFile=null;
				try{
					directoryFile = new File(directory);
					if(!directoryFile.exists()){
						directoryFile.mkdir();
					}
					outFile = File.createTempFile(prefix, suffix, directoryFile);
				}catch(Exception e){
					outFile = File.createTempFile(prefix, suffix);
				}

				fos = new FileOutputStream(outFile);
				message.writeTo(fos);
				log.info("error mail saved: "+outFile.getAbsolutePath());
			} catch (IOException e) {
				log.error("error creating mail error file",e);
			}catch (MessagingException e) {
				log.error("error saving mail error file",e);
			}finally{
				if(fos!=null){
					try{
						fos.close();
					}catch (IOException e){
						log.error("error closing mail error file",e);
					}
				}
			}
		}
	}
	
}

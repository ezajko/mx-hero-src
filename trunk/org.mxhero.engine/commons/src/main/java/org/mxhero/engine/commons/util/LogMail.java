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

package org.mxhero.engine.commons.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author mmarmol
 */
public abstract class LogMail {

	private static Logger log = LoggerFactory.getLogger(LogMail.class);
	
	/**
	 * @param message
	 * @param prefix
	 * @param suffix
	 * @param directory
	 */
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

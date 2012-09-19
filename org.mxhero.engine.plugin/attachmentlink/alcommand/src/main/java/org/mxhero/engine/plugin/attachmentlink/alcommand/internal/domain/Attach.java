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

package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.Part;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author royojp
 * @version 1.0
 * @created 22-Aug-2011 12:52:23
 */
public class Attach {
	
	private static Logger log = Logger.getLogger(Attach.class);

	private Long id;
	private String md5Checksum;
	private String fileName;
	private Long size;
	private String mimeType;
	private String path;
	private List<MessageAttachRecipient> messageAttachRecipient;
	private String tempLink;


	public Attach(){
		this.messageAttachRecipient = new ArrayList<MessageAttachRecipient>();
	}


	public String getMd5Checksum() {
		return md5Checksum;
	}


	public void setMd5Checksum(String md5Checksum) {
		this.md5Checksum = md5Checksum;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getBeautySize(){
		StringBuffer buff = new StringBuffer();
		String suffix = "b";
		long sizeBeauty = getSize().longValue();
		long finalResult = sizeBeauty;
		int measure = 0;
		while(finalResult>1023){
			finalResult = finalResult/1024;
			measure++;
		}
		suffix = getMeasure(measure);
		buff.append(finalResult);
		buff.append(" ");
		buff.append(suffix);
		return buff.toString();
	}
	


	private String getMeasure(int measure) {
		String m = null;
		switch(measure){
		case 1: 
			m = "Kb";
			break;
		case 2:
			m = "Mb";
			break;
		case 3:
			m = "Gb";
			break;
		case 4:
			m = "Tb";
			break;
		default:
			m = "";
		}
		return m;
	}


	public Long getSize() {
		return size;
	}


	public void setSize(Long size) {
		this.size = size;
	}


	public String getMimeType() {
		return mimeType;
	}


	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public List<MessageAttachRecipient> getMessageAttachRecipient() {
		return messageAttachRecipient;
	}


	public void setMessageAttachRecipient(
			List<MessageAttachRecipient> messageAttachRecipient) {
		this.messageAttachRecipient = messageAttachRecipient;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id){
		this.id = id;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((md5Checksum == null) ? 0 : md5Checksum.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attach other = (Attach) obj;
		if (md5Checksum == null) {
			if (other.md5Checksum != null)
				return false;
		} else if (!md5Checksum.equals(other.md5Checksum))
			return false;
		return true;
	}


	public void buildPath(Part part, String baseStorePath) throws MessagingException {
		StringBuffer buffer = new StringBuffer(baseStorePath);
		buffer.append("/");
		buffer.append(getMd5Checksum());
		buffer.append(getExtension());
		File file = new File(buffer.toString());
		if(!file.exists()){
			saveFile(part, buffer);
		}else{
			setSize(new Long(part.getSize()));
		}
		setPath(buffer.toString());
	}


	private void saveFile(Part part, StringBuffer buffer) {
		BufferedOutputStream outS = null;
		InputStream is = null;
        byte[] buff = new byte[2048];
		try {
			outS = new BufferedOutputStream(new FileOutputStream(buffer.toString()));
	        is = part.getInputStream();
	        int ret = 0, count = 0;
	        while( (ret = is.read(buff)) > 0 ){
	                outS.write(buff, 0, ret);
	                count += ret;
	        }
			setSize(new Long(count));
		} catch (Exception e) {
			log.error("Could not store attach in the filesystem. Verify the configuration of your filesystem properties. Attach: "+getFileName()+" - "+e.getClass().getName()+" - "+e.getMessage());
			throw new RuntimeException("Could not store attach in filesystem. Attach "+getFileName());
		}finally{
			if(outS != null)
				try {
					outS.close();
				} catch (IOException e) {
				}
			if(is != null)
				try {
					is.close();
				} catch (IOException e) {}
		}
	}


	private String getExtension() {
		String extension = "";
		if(!StringUtils.isEmpty(getFileName())){
			int indexOf = getFileName().indexOf(".");
			if(indexOf != -1){
				extension = getFileName().substring(indexOf);
				if(extension.length()>3){
					extension = extension.substring(0, 4);
				}
			}
		}
		return extension;
	}


	public void buildMd5Checksum(InputStream inputStream) {
		String checksum = "";
		try {
			checksum = DigestUtils.md5Hex(inputStream);
			setMd5Checksum(checksum);
		} catch (IOException e) {
			log.error("Could not build the Checksum code for the file. Attach: "+getFileName()+" - "+e.getClass().getName()+" - "+e.getMessage());
			throw new RuntimeException("Could not obtain md5 checksum from file "+getFileName());
		}
	}


	public void setTempLink(String createLink) {
		this.tempLink = createLink;	
	}
	
	public String getTempLink() {
		return tempLink;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Checksum: ");
		builder.append(getMd5Checksum());
		builder.append(" - ");
		builder.append("Locale: ");
		builder.append(getFileName());
		builder.append(" - ");
		builder.append("Mime: ");
		builder.append(getMimeType());
		builder.append(" - ");
		builder.append("Size: ");
		builder.append(getSize());
		builder.append(" - ");
		builder.append("Path: ");
		builder.append(getPath());
		return builder.toString();
	};
}

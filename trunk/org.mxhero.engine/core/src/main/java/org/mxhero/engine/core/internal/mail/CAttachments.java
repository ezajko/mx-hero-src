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

package org.mxhero.engine.core.internal.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Attachments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mmarmol
 *
 */
public class CAttachments implements Attachments {

	private static Logger log = LoggerFactory.getLogger(CAttachments.class);
	
	private MimeMail mimeMail;

	/**
	 * @param mimeMail
	 */
	public CAttachments(MimeMail mimeMail) {
		this.mimeMail = mimeMail;
	}

	@Override
	public boolean isAttached() {
		try {
			return MailUtils.hasAttachments(mimeMail.getMessage());
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public Collection<String> getTypes() {
		Collection<String> types = new ArrayList<String>();
		try {
			MailUtils.addTypes(this.mimeMail.getMessage(), types);
		}catch(Exception e){log.warn(e.getMessage());}
		return types;
	}

	@Override
	public Collection<String> getFileNames() {
		Collection<String> files = new ArrayList<String>();
		try {
			MailUtils.addFileNames(this.mimeMail.getMessage(), files);
		}catch(Exception e){log.warn(e.getMessage());}
		return files;
	}

	@Override
	public Collection<String> getExtensions() {
		Collection<String> extension = new ArrayList<String>();
		for(String fileName : getFileNames()){
			int i = fileName.lastIndexOf('.');
			if (i > 0 && i < fileName.length() - 1){
				extension.add(fileName.substring(i+1).toLowerCase(Locale.ENGLISH));
			}
		}	
		return extension;
	}

	@Override
	public boolean hasMatchingType(String... patterns) {
		if(patterns != null){
			for(String type :getTypes()){
				for(String pattern : patterns){
					if(type.startsWith(pattern)){
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean hasMatchingName(String... patterns) {
		if(patterns != null){
			for(String name :getFileNames()){
				for(String pattern : patterns){
					if(name.matches(pattern)){
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean hasMatchingExtension(String... patterns) {
		if(patterns != null){
			for(String extension :getExtensions()){
				for(String pattern : patterns){
					if(extension.matches(pattern)){
						return true;
					}
				}
			}
		}
		return false; 
	}

	@Override
	public List<String> removeByName(String... names) {
		List<String> removed = new ArrayList<String>();
		return removed;
	}

	@Override
	public List<String> removeByExtension(String... extensions) {
		List<String> removed = new ArrayList<String>();
		if(extensions!=null){
			try {
				MailUtils.removeByExtensions(mimeMail.getMessage(), null,new HashSet<String>(Arrays.asList(extensions)), removed);
			} catch (Exception e) {log.warn(e.getMessage());}
		}
		return removed;
	}

	@Override
	public List<String> removeByType(String... types) {
		List<String> removed = new ArrayList<String>();
		if(types!=null){
			try {
				MailUtils.removeByTypes(mimeMail.getMessage(), null,new HashSet<String>(Arrays.asList(types)), removed);
			} catch (Exception e) {log.warn(e.getMessage());}
		}
		return removed;
	}

}

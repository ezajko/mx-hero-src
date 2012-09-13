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

package org.mxhero.engine.plugin.dbfinder.internal.service;

import java.util.HashSet;

import org.mxhero.engine.commons.domain.Domain;
import org.mxhero.engine.commons.domain.User;
import org.mxhero.engine.commons.finders.UserFinder;
import org.mxhero.engine.plugin.dbfinder.internal.repository.UserRepository;
import org.mxhero.engine.plugin.dbfinder.internal.util.Cloner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mmarmol
 *
 */
public class MappedUserFinder implements UserFinder
{
	private static Logger log = LoggerFactory.getLogger(MappedUserFinder.class);
	private UserRepository repository;
	
	/* (non-Javadoc)
	 * @see org.mxhero.engine.commons.finders.UserFinder#getUser(java.lang.String)
	 */
	@Override
	public User getUser(String mailAdress) {
		String formatedMail = null;
		User user = null;
		Domain userDomain = null;
		if(mailAdress==null || mailAdress.trim().isEmpty() ||
				mailAdress.split("@").length!=2){
			return null;
		}
		formatedMail = mailAdress.trim().toLowerCase();
		String domain = formatedMail.split("@")[1];
		
		user=Cloner.clone(repository.getUsers().get(formatedMail));
		
		if(user==null){
			userDomain=Cloner.clone(repository.getDomains().get(domain));
			if(userDomain==null){
				userDomain = new Domain();
				userDomain.setId(domain);
				userDomain.setManaged(false);
				userDomain.setAliases(new HashSet<String>());
				userDomain.getAliases().add(domain);
			}
			user=new User();
			user.setMail(formatedMail);
			user.setManaged(false);
			user.setDomain(userDomain);
			user.setAliases(new HashSet<String>());
			user.getAliases().add(mailAdress);
		}
		if(log.isDebugEnabled()){
			log.debug("found "+user.toString()+" and "+user.getDomain().toString());
		}
		return user;
	}

	/**
	 * @return
	 */
	public UserRepository getRepository() {
		return repository;
	}

	/**
	 * @param repository
	 */
	public void setRepository(UserRepository repository) {
		this.repository = repository;
	}

}

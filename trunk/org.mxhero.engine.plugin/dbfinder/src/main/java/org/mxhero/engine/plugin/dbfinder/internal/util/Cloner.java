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

package org.mxhero.engine.plugin.dbfinder.internal.util;

import java.util.HashMap;
import java.util.HashSet;

import org.mxhero.engine.commons.domain.Domain;
import org.mxhero.engine.commons.domain.User;

/**
 * @author mmarmol
 *
 */
public abstract class Cloner {

	/**
	 * 
	 */
	private Cloner(){
	}
	
	/**
	 * @param domain
	 * @return
	 */
	public static Domain clone(Domain domain){
		Domain clonedDomain = null;
		if(domain!=null){
			clonedDomain = new Domain();
			clonedDomain.setId(domain.getId());
			clonedDomain.setManaged(domain.getManaged());
			clonedDomain.setAliases(new HashSet<String>());
			if(domain.getAliases()!=null){
				clonedDomain.getAliases().addAll(domain.getAliases());
			}
		}
		return clonedDomain;
	}
	
	/**
	 * @param user
	 * @return
	 */
	public static User clone(User user){
		User clonedUser = null;
			if(user!=null){
				clonedUser = new User();
				clonedUser.setMail(user.getMail());
				clonedUser.setManaged(user.getManaged());
				clonedUser.setGroup(user.getGroup());
				clonedUser.setDomain(clone(user.getDomain()));
				clonedUser.setAliases(new HashSet<String>());
				clonedUser.setAddressBook(new HashSet<String>());
				clonedUser.setProperties(new HashMap<String, String>());
				if(user.getAliases()!=null){
					clonedUser.getAliases().addAll(user.getAliases());
				}
				if(user.getAddressBook()!=null){
					clonedUser.getAddressBook().addAll(user.getAddressBook());
				}
				if(user.getProperties()!=null){
					clonedUser.getProperties().putAll(user.getProperties());
				}
			}
		return clonedUser;
	}
}

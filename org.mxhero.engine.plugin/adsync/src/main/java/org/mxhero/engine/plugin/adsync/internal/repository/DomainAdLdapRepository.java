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

package org.mxhero.engine.plugin.adsync.internal.repository;

import java.util.List;
import java.util.Map;

import org.mxhero.engine.plugin.adsync.internal.domain.DomainAdLdap;

public interface DomainAdLdapRepository {

	public DomainAdLdap findDomainAdLdap(String domainId);
	
	public List<String> findDomainsToSync();
	
	public List<String> findDomainAliases(String domainId);
	
	public void updateNextAdLdapCheck(String domainId);
	
	public void updateErrorAdLdapCheck(String domainId, String lastError);
	
	public void deleteAccount(String account, String domainId);
	
	public List<String> getManagedAccounts(String domainId);
	
	public List<String> getNotManagedAccounts(String domainId);
	
	public void updateAliasesAccount(String account, String domainId, List<String> aliases);
	
	public void insertAccount(String account, String domainId, List<String> aliases);
	
	public void refreshProperties(String account, String domainId, Map<String, String> properties);
}

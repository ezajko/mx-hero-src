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
package org.mxhero.console.frontend.domain
{
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.messaging.Channel;
	import mx.messaging.ChannelSet;
	
	import org.mxhero.console.commons.feature.IApplicationContext;
	import org.mxhero.console.frontend.application.event.GetAccountsEvent;
	import org.mxhero.console.frontend.application.event.GetDomainsEvent;

	[Bindable]
	public class ApplicationContext implements IApplicationContext
	{

		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Inject]
		public var defaultChannelApplication:ChannelSet;
		
		public var applicationUser:ApplicationUser;
		
		public var configuration:Configuration;
		
		private var _selectedDomain:Domain;
		
		private var _ruleDomain:String;

		public var locales:ArrayCollection;
		
		private var _groups:ArrayCollection;

		private var _accounts:ArrayCollection;
		
		private var _accountsAliases:ArrayCollection;

		private var _domains:ArrayCollection;

		private var _domainsAliases:ArrayCollection;

		
		public function get selectedDomain():Domain
		{
			return _selectedDomain;
		}
		
		public function set selectedDomain(value:Domain):void
		{
			_selectedDomain = value;
			if(_selectedDomain!=null){
				ruleDomain=_selectedDomain.domain;
			}
		}
		
		public function get ruleDomain():String
		{
			return _ruleDomain;
		}
		
		public function set ruleDomain(value:String):void
		{
			_ruleDomain = value;
		}
		
		public function get groups():ArrayCollection
		{
			return _groups;
		}

		public function set groups(value:ArrayCollection):void
		{
			_groups = value;
		}		
		
		public function get accounts():ArrayCollection
		{
			return _accounts;
		}
		
		public function get accountsAliases():ArrayCollection
		{
			return _accountsAliases;
		}

		public function set accountsAliases(value:ArrayCollection):void
		{
			this._accountsAliases=value;
		}
		
		public function set accounts(value:ArrayCollection):void
		{
			var newAliases:ArrayCollection;
			if(value!=null){
				newAliases = new ArrayCollection();
				for each(var account:Object in value){
					if(account.aliases!=null){
						for each(var alias:Object in account.aliases){
							newAliases.addItem(alias);
						}
					}
				}
			}
			_accounts = value;
			accountsAliases = newAliases;
		}
		
		public function get domains():ArrayCollection
		{
			return _domains;
		}

		public function get domainsAliases():ArrayCollection{
			return _domainsAliases;
		}
		
		public function set domainsAliases(value:ArrayCollection):void
		{
			this._domainsAliases = value;
		}
		
		public function set domains(value:ArrayCollection):void
		{
			var newAliases:ArrayCollection;
			if(value!=null){
				newAliases = new ArrayCollection();
				for each(var domain:Object in value){
					if(domain.aliases!=null){
						for each(var alias:Object in domain.aliases){
							var aliasVO:DomainAlias = new DomainAlias();
							aliasVO.alias=alias as String;
							aliasVO.domain=domain as Domain;
							newAliases.addItem(aliasVO);
						}
					}
				}
			}
			domainsAliases = newAliases;
			_domains = value;
		}

		public function refreshAccounts(email:String):void{
			if(selectedDomain!=null){
				dispatcher(new GetAccountsEvent(selectedDomain.domain,email));
			}else{
				dispatcher(new GetAccountsEvent(null,email));
			}
		}
		
		public function refreshDomains(domain:String):void{
			dispatcher(new GetDomainsEvent(domain));
		}
		
	}
}
package org.mxhero.console.frontend.domain
{
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	
	import org.mxhero.console.commons.feature.IApplicationContext;
	import org.mxhero.console.frontend.application.event.GetAccountsEvent;
	import org.mxhero.console.frontend.application.event.GetDomainsEvent;

	[Bindable]
	public class ApplicationContext implements IApplicationContext
	{

		[MessageDispatcher]
		public var dispatcher:Function;
		
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
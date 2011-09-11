package org.mxhero.console.frontend.domain
{
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;

	[Bindable]
	public class ApplicationContext
	{

		public var applicationUser:ApplicationUser;
		
		public var configuration:Configuration;
		
		public var selectedDomain:Domain;
		
		public var groups:ArrayCollection;
		
		public var locales:ArrayCollection;
		
		private var _accounts:ArrayCollection;
		
		public var accountsAliases:ArrayCollection;
		
		private var _domains:ArrayCollection;
		
		public var domainsAliases:ArrayCollection;

		public function get accounts():ArrayCollection
		{
			return _accounts;
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


	}
}
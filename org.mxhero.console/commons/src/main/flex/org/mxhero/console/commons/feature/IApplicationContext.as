package org.mxhero.console.commons.feature
{
	import mx.collections.ArrayCollection;

	[Bindable]
	public interface IApplicationContext
	{
		function get ruleDomain():String;
		
		function get domains():ArrayCollection;
		
		function get domainsAliases():ArrayCollection;
		
		function get accounts():ArrayCollection;
		
		function get accountsAliases():ArrayCollection;
		
		function get groups():ArrayCollection;
		
		function refreshAccounts(email:String):void;
		
		function refreshDomains(domain:String):void;

	}
}
package org.mxhero.console.frontend.application.event
{
	public class GetAccountsEvent
	{
		public var domainId:String;
		public var filterAccount:String;
		
		public function GetAccountsEvent(domainId:String=null,filterAccount:String=null)
		{
			this.filterAccount=filterAccount;
			this.domainId=domainId;
		}
	}
}
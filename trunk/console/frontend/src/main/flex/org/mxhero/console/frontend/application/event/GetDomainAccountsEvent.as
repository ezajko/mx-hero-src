package org.mxhero.console.frontend.application.event
{
	public class GetDomainAccountsEvent
	{
		public var domainId:String;
		
		public function GetDomainAccountsEvent(domainId:String)
		{
			this.domainId=domainId;
		}
	}
}
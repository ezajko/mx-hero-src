package org.mxhero.console.features.application.event
{
	public class GetDomainAccountsEvent
	{
		public var domainId:Number;
		
		public function GetDomainAccountsEvent(domainId:Number)
		{
			this.domainId=domainId;
		}
	}
}
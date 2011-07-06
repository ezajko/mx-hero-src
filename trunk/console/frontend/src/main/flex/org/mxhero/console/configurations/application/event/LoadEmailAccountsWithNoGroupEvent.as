package org.mxhero.console.configurations.application.event
{
	public class LoadEmailAccountsWithNoGroupEvent
	{
		public var domainId:String;
		
		public function LoadEmailAccountsWithNoGroupEvent(domainId:String)
		{
			this.domainId=domainId;
		}
	}
}
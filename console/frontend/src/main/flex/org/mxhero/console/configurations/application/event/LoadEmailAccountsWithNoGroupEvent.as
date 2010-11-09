package org.mxhero.console.configurations.application.event
{
	public class LoadEmailAccountsWithNoGroupEvent
	{
		public var domainId:Number;
		
		public function LoadEmailAccountsWithNoGroupEvent(domainId:Number)
		{
			this.domainId=domainId;
		}
	}
}
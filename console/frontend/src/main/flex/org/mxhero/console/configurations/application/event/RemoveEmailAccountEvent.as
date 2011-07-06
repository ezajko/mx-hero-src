package org.mxhero.console.configurations.application.event
{
	public class RemoveEmailAccountEvent
	{
		public var account:String;
		public var domainId:String;
		
		public function RemoveEmailAccountEvent(account:String,domainId:String)
		{
			this.account=account;
			this.domainId=domainId;
		}
	}
}
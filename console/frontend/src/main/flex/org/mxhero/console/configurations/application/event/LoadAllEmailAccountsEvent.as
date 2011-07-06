package org.mxhero.console.configurations.application.event
{
	public class LoadAllEmailAccountsEvent
	{
		public var domainId:String;
		
		public var email:String;
		
		public var groupId:String;
		
		public function LoadAllEmailAccountsEvent(domainId:String,email:String,groupId:String)
		{
			this.domainId=domainId;
			this.email=email;
			this.groupId=groupId;
		}
	}
}
package org.mxhero.console.configurations.application.event
{
	public class LoadEmailAccountsByGroupEvent
	{
		public var groupName:String;
		public var domainId:String;
		
		public function LoadEmailAccountsByGroupEvent(groupName:String,domainId:String)
		{
			this.groupName=groupName;
			this.domainId=domainId;
		}
	}
}
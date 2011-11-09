package org.mxhero.console.configurations.application.event
{
	public class RemoveGroupEvent
	{
		public var groupName:String;
		public var domainId:String;
		
		public function RemoveGroupEvent(groupName:String,domainId:String)
		{
			this.groupName=groupName;
			this.domainId=domainId;
		}
	}
}
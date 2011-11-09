package org.mxhero.console.configurations.application.event
{
	public class LoadAllGroupsEvent
	{
		public var domainId:String;
		
		public function LoadAllGroupsEvent(domainId:String)
		{
			this.domainId=domainId;
		}
	}
}
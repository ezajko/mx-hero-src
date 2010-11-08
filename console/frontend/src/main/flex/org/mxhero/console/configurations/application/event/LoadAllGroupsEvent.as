package org.mxhero.console.configurations.application.event
{
	public class LoadAllGroupsEvent
	{
		public var domainId:Number;
		
		public function LoadAllGroupsEvent(domainId:Number)
		{
			this.domainId=domainId;
		}
	}
}
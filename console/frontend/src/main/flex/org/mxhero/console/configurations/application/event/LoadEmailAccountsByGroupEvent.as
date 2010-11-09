package org.mxhero.console.configurations.application.event
{
	public class LoadEmailAccountsByGroupEvent
	{
		public var groupId:Number;
		
		public function LoadEmailAccountsByGroupEvent(groupId:Number)
		{
			this.groupId=groupId;
		}
	}
}
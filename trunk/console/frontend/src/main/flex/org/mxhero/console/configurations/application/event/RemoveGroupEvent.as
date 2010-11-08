package org.mxhero.console.configurations.application.event
{
	public class RemoveGroupEvent
	{
		public var groupId:Number;
		
		public function RemoveGroupEvent(groupId:Number)
		{
			this.groupId=groupId;
		}
	}
}
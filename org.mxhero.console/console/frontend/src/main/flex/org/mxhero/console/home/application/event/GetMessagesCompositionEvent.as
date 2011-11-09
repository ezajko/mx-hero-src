package org.mxhero.console.home.application.event
{
	public class GetMessagesCompositionEvent
	{
		public var since:Date;
		public var domainId:String;
		
		public function GetMessagesCompositionEvent(since:Date,domainId:String)
		{
			this.since=since;
			this.domainId=domainId;
		}
	}
}
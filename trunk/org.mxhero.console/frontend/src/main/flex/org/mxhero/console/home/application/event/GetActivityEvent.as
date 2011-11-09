package org.mxhero.console.home.application.event
{
	public class GetActivityEvent
	{
		public var since:Date;
		public var domainId:String;
		
		public function GetActivityEvent(since:Date, domainId:String)
		{
			this.since=since;
			this.domainId=domainId;
		}
	}
}
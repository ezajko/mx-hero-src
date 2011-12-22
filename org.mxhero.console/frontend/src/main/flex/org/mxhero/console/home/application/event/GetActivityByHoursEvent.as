package org.mxhero.console.home.application.event
{
	public class GetActivityByHoursEvent
	{
		public var since:Date;
		public var domainId:String;
		
		public function GetActivityByHoursEvent(since:Date, domainId:String)
		{
			this.since=since;
			this.domainId=domainId;
		}
	}
}
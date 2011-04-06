package org.mxhero.console.reports.application.event
{
	public class GetIncommingEvent
	{
		public var domainId:String;
		public var since:Date;
		
		public function GetIncommingEvent(domainId:String,since:Date)
		{
			this.domainId=domainId;
			this.since=since;
		}
	}
}
package org.mxhero.console.reports.application.event
{
	public class GetOutgoingEvent
	{
		public var domainId:String;
		public var since:Date;
		
		public function GetOutgoingEvent(domainId:String,since:Date)
		{
			this.domainId=domainId;
			this.since=since;
		}
	}
}
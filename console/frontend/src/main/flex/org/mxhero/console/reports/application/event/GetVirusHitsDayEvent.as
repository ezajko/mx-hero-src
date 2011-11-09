package org.mxhero.console.reports.application.event
{
	public class GetVirusHitsDayEvent
	{
		public var domain:String;
		public var since:Date;
		
		public function GetVirusHitsDayEvent(domain:String,since:Date)
		{
			this.domain=domain;
			this.since=since;
		}
	}
}
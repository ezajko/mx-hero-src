package org.mxhero.console.reports.application.event
{
	public class GetSpamHitsDayEvent
	{
		public var domain:String;
		public var since:Date;
		
		public function GetSpamHitsDayEvent(domain:String,since:Date)
		{
			this.domain=domain;
			this.since=since;
		}
	}
}
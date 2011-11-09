package org.mxhero.console.reports.application.event
{
	public class GetSpamHitsEvent
	{
		public var domain:String;
		public var since:Date;
		
		public function GetSpamHitsEvent(domain:String,since:Date)
		{
			this.domain=domain;
			this.since=since;
		}
	}
}
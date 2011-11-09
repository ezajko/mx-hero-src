package org.mxhero.console.reports.application.event
{
	public class GetVirusHitsEvent
	{
		public var domain:String;
		public var since:Date;
		
		public function GetVirusHitsEvent(domain:String,since:Date)
		{
			this.domain=domain;
			this.since=since;
		}
	}
}
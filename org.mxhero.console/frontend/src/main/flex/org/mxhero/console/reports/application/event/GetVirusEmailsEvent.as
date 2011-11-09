package org.mxhero.console.reports.application.event
{
	public class GetVirusEmailsEvent
	{
		public var domain:String;
		public var since:Date;
		public var until:Date;
		
		public function GetVirusEmailsEvent(domain:String,since:Date,until:Date)
		{
			this.domain=domain;
			this.since=since;
			this.until=until;
		}
	}
}
package org.mxhero.console.reports.application.event
{
	public class GetSpamEmailsEvent
	{
		public var domain:String;
		public var since:Date;
		public var until:Date;
		
		public function GetSpamEmailsEvent(domain:String,since:Date,until:Date)
		{
			this.domain=domain;
			this.since=since;
			this.until=until;
		}
	}
}
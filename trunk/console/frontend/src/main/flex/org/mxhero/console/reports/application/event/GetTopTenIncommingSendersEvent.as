package org.mxhero.console.reports.application.event
{
	public class GetTopTenIncommingSendersEvent
	{
		public var domainId:String;
		public var since:Date
		
		public function GetTopTenIncommingSendersEvent(domainId:String,since:Date)
		{
			this.domainId=domainId;
			this.since=since;
		}
	}
}
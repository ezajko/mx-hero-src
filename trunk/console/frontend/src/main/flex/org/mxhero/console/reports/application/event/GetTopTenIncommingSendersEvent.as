package org.mxhero.console.reports.application.event
{
	public class GetTopTenIncommingSendersEvent
	{
		public var domainId:String;
		public var since:Date
		public var onlyDomain:Boolean;
		
		public function GetTopTenIncommingSendersEvent(domainId:String,since:Date,onlyDomain:Boolean)
		{
			this.domainId=domainId;
			this.since=since;
			this.onlyDomain=onlyDomain;
		}
	}
}
package org.mxhero.console.reports.application.event
{
	public class GetTopTenOutgoingRecipientsEvent
	{
		public var domainId:String;
		public var since:Date;
		
		public function GetTopTenOutgoingRecipientsEvent(domainId:String, since:Date)
		{
			this.domainId=domainId;
			this.since=since;
		}
	}
}
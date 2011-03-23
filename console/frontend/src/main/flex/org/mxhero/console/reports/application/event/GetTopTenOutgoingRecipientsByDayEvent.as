package org.mxhero.console.reports.application.event
{
	public class GetTopTenOutgoingRecipientsByDayEvent
	{
		public var domainId:String;
		public var day:Date;
		
		public function GetTopTenOutgoingRecipientsByDayEvent(domainId:String, day:Date)
		{
			this.domainId=domainId;
			this.day=day;
		}
	}
}
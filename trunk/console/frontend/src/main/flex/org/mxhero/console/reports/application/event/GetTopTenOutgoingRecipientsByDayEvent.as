package org.mxhero.console.reports.application.event
{
	public class GetTopTenOutgoingRecipientsByDayEvent
	{
		public var domainId:String;
		public var day:Date;
		public var onlyDomain:Boolean;
		
		public function GetTopTenOutgoingRecipientsByDayEvent(domainId:String, day:Date, onlyDomain:Boolean)
		{
			this.domainId=domainId;
			this.day=day;
			this.onlyDomain=onlyDomain;
		}
	}
}
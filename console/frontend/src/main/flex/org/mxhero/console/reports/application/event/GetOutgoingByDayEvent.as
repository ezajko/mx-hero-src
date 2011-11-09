package org.mxhero.console.reports.application.event
{
	public class GetOutgoingByDayEvent
	{
		public var domainId:String;
		public var day:Date;
		
		public function GetOutgoingByDayEvent(domainId:String,day:Date)
		{
			this.domainId=domainId;
			this.day=day;
		}
	}
}
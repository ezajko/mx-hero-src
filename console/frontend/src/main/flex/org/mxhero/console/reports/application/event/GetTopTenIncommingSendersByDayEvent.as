package org.mxhero.console.reports.application.event
{
	public class GetTopTenIncommingSendersByDayEvent
	{
		public var domainId:String;
		public var day:Date;
		
		public function GetTopTenIncommingSendersByDayEvent(domainId:String,day:Date)
		{
			this.domainId=domainId;
			this.day=day;
		}
	}
}
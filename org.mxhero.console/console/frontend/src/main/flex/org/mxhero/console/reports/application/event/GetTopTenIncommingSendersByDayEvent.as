package org.mxhero.console.reports.application.event
{
	public class GetTopTenIncommingSendersByDayEvent
	{
		public var domainId:String;
		public var day:Date;
		public var onlyDomain:Boolean;
		
		public function GetTopTenIncommingSendersByDayEvent(domainId:String,day:Date,onlyDomain:Boolean)
		{
			this.domainId=domainId;
			this.day=day;
			this.onlyDomain=onlyDomain;
		}
	}
}
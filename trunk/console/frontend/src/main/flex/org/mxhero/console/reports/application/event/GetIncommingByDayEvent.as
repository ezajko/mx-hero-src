package org.mxhero.console.reports.application.event
{
	public class GetIncommingByDayEvent
	{
		
		public var domainId:String=null;
		public var day:Date=null;
		
		public function GetIncommingByDayEvent(domainId:String,day:Date)
		{
			this.domainId=domainId;
			this.day=day;
		}
	}
}
package org.mxhero.console.reports.application.event
{
	public class GetIncommingEvent
	{
		public var domainId:String;
		
		public function GetIncommingEvent(domainId:String=null)
		{
			this.domainId=domainId;
		}
	}
}
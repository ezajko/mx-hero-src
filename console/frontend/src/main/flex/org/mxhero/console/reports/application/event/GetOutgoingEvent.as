package org.mxhero.console.reports.application.event
{
	public class GetOutgoingEvent
	{
		public var domainId:String;
		
		public function GetOutgoingEvent(domainId:String=null)
		{
			this.domainId=domainId;
		}
	}
}
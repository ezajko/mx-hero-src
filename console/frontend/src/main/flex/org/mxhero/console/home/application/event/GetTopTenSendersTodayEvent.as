package org.mxhero.console.home.application.event
{
	public class GetTopTenSendersTodayEvent
	{
		public var domainId:String;
		
		public function GetTopTenSendersTodayEvent(domainId:String)
		{
			this.domainId=domainId;
		}
	}
}
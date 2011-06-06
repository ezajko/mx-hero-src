package org.mxhero.console.home.application.event
{
	public class GetTopTenRecipientsTodayEvent
	{
		public var domainId:String;
		
		public function GetTopTenRecipientsTodayEvent(domainId:String)
		{
			this.domainId=domainId;
		}
	}
}
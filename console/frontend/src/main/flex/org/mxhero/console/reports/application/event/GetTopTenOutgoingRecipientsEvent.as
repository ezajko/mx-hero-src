package org.mxhero.console.reports.application.event
{
	public class GetTopTenOutgoingRecipientsEvent
	{
		public var domainId:String;
		
		public function GetTopTenOutgoingRecipientsEvent(domainId:String=null)
		{
			this.domainId=domainId;
		}
	}
}
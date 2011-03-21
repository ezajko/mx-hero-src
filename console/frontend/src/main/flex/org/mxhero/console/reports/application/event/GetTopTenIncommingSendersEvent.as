package org.mxhero.console.reports.application.event
{
	public class GetTopTenIncommingSendersEvent
	{
		public var domainId:String;
		
		public function GetTopTenIncommingSendersEvent(domainId:String=null)
		{
			this.domainId=domainId;
		}
	}
}
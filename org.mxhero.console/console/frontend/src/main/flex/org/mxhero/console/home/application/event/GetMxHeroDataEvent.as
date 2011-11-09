package org.mxhero.console.home.application.event
{
	public class GetMxHeroDataEvent
	{
		public var domainId:String;
		
		public function GetMxHeroDataEvent(domainId:String)
		{
			this.domainId=domainId;
		}
	}
}
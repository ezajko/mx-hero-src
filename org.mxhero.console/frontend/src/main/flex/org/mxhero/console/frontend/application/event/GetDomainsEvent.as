package org.mxhero.console.frontend.application.event
{
	public class GetDomainsEvent
	{
		public var domainFilter:String;
		public function GetDomainsEvent(domainFilter:String=null)
		{
			this.domainFilter=domainFilter;
		}
	}
}
package org.mxhero.console.frontend.application.event
{
	public class GetDomainGroupsEvent
	{
		public var domainId:String;
		
		public function GetDomainGroupsEvent(domainId:String)
		{
			this.domainId=domainId;
		}
	}
}
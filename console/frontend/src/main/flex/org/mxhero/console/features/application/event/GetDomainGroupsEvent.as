package org.mxhero.console.features.application.event
{
	public class GetDomainGroupsEvent
	{
		public var domainId:Number;
		
		public function GetDomainGroupsEvent(domainId:Number)
		{
			this.domainId=domainId;
		}
	}
}
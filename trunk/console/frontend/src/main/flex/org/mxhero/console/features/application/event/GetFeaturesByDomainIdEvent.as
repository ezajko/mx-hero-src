package org.mxhero.console.features.application.event
{
	public class GetFeaturesByDomainIdEvent
	{
		public var domainId:Number;
		
		public function GetFeaturesByDomainIdEvent(domainId:Number)
		{
			this.domainId=domainId;
		}
	}
}
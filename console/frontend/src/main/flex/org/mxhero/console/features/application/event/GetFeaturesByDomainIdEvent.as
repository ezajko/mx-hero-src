package org.mxhero.console.features.application.event
{
	public class GetFeaturesByDomainIdEvent
	{
		public var domainId:String;
		
		public function GetFeaturesByDomainIdEvent(domainId:String)
		{
			this.domainId=domainId;
		}
	}
}
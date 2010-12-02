package org.mxhero.console.features.application.event
{
	public class GetRulesByDomainIdEvent
	{
		public var featureId:Number;
		
		public var domainId:Number;
		
		public function GetRulesByDomainIdEvent(featureId:Number, domainId:Number)
		{
			this.featureId=featureId;
			this.domainId=domainId;
		}
	}
}
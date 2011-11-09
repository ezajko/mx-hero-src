package org.mxhero.console.features.application.event
{
	public class GetRulesByDomainIdEvent
	{
		public var featureId:Number;
		
		public var domainId:String;
		
		public function GetRulesByDomainIdEvent(featureId:Number, domainId:String)
		{
			this.featureId=featureId;
			this.domainId=domainId;
		}
	}
}
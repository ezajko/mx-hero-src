package org.mxhero.console.features.application.event
{
	import org.mxhero.console.frontend.domain.FeatureRule;

	public class CreateDomainRuleEvent
	{
		public var rule:FeatureRule;
		
		public var featureId:Number;
		
		public var domainId:Number;
		
		public function CreateDomainRuleEvent(rule:FeatureRule, featureId:Number, domainId:Number)
		{
			this.rule=rule;
			this.featureId=featureId;
			this.domainId=domainId;
		}
	}
}
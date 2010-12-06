package org.mxhero.console.features.application.event
{
	import org.mxhero.console.frontend.domain.FeatureRule;

	public class CreateNoDomainRuleEvent
	{
		public var rule:FeatureRule;
		
		public var featureId:Number;
		
		public function CreateNoDomainRuleEvent(rule:FeatureRule, featureId:Number)
		{
			this.rule=rule;
			this.featureId=featureId;
		}
	}
}
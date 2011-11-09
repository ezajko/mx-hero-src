package org.mxhero.console.features.application.event
{
	import org.mxhero.console.frontend.domain.FeatureRule;

	public class EditRuleEvent
	{
		public var rule:FeatureRule;
		
		public function EditRuleEvent(rule:FeatureRule)
		{
			this.rule=rule;
		}
	}
}
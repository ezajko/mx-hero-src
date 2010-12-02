package org.mxhero.console.features.application.event
{
	public class ToggleRuleStatusEvent
	{
		public var ruleId:Number;
		
		public function ToggleRuleStatusEvent(ruleId:Number)
		{
			this.ruleId=ruleId;
		}
	}
}
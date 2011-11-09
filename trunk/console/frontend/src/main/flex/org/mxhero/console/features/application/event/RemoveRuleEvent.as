package org.mxhero.console.features.application.event
{
	public class RemoveRuleEvent
	{
		public var ruleId:Number;
		
		public function RemoveRuleEvent(ruleId:Number)
		{
			this.ruleId=ruleId;
		}
	}
}
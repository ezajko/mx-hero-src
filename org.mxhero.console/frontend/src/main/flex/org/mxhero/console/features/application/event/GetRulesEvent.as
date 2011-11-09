package org.mxhero.console.features.application.event
{
	public class GetRulesEvent
	{
		public var featureId:Number;
		
		public function GetRulesEvent(featureId:Number)
		{
			this.featureId=featureId;
		}
	}
}
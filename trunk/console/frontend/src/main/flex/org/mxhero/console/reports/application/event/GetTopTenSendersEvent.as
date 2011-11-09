package org.mxhero.console.reports.application.event
{
	import org.mxhero.console.frontend.domain.FeatureRuleDirection;

	public class GetTopTenSendersEvent
	{
		public var from:FeatureRuleDirection;
		public var to:FeatureRuleDirection;
		public var since:Number;
		public var until:Number;
		
		public function GetTopTenSendersEvent(from:FeatureRuleDirection
									   ,to:FeatureRuleDirection
										,since:Number
										 ,until:Number)
		{
			this.from=from;
			this.to=to;
			this.since=since;
			this.until=until;
		}

	}
}
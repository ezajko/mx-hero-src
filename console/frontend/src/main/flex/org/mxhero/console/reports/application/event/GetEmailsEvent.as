package org.mxhero.console.reports.application.event
{
	import org.mxhero.console.frontend.domain.FeatureRuleDirection;

	public class GetEmailsEvent
	{
		public var from:FeatureRuleDirection;
		public var to:FeatureRuleDirection;
		public var since:Date;
		public var until:Date;
		
		public function GetEmailsEvent(from:FeatureRuleDirection
									    ,to:FeatureRuleDirection
										,since:Date
										,until:Date)
		{
			this.from=from;
			this.to=to;
			this.since=since;
			this.until=until;
		}
	}
}
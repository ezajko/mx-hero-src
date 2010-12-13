package org.mxhero.console.frontend.domain
{
	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.FeatureRuleVO")]
	public class FeatureRule
	{
		public var id:Number;
		public var name:String;
		public var created:Date;
		public var updated:Date;
		public var enabled:Boolean;
		public var fromDirection:FeatureRuleDirection;
		public var toDirection:FeatureRuleDirection;
		
		public function clone():FeatureRule{
			var newRule:FeatureRule = new FeatureRule();
			newRule.created = this.created;
			newRule.enabled = this.enabled;
			newRule.id = this.id;
			newRule.updated = this.updated;
			newRule.name = this.name;
			if(this.fromDirection!=null){
				newRule.fromDirection = this.fromDirection.clone();
			} else {
				newRule.fromDirection = new FeatureRuleDirection();
			}
			if( this.toDirection!=null){
				newRule.toDirection = this.toDirection.clone();
			} else {
				newRule.toDirection = new FeatureRuleDirection();
			}
			return newRule;
		}
		
	}
}
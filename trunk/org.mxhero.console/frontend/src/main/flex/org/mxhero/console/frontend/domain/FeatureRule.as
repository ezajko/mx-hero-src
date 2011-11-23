package org.mxhero.console.frontend.domain
{
	import mx.collections.ArrayCollection;
	
	import org.mxhero.console.commons.feature.FeatureRuleProperty;

	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.FeatureRuleVO")]
	public class FeatureRule
	{
		public var id:Number;
		public var name:String;
		public var created:Date;
		public var updated:Date;
		public var enabled:Boolean;
		public var adminOrder:String;
		public var featureId:Number;
		public var domain:String;
		public var fromDirection:FeatureRuleDirection;
		public var toDirection:FeatureRuleDirection;
		public var properties:ArrayCollection;
		public var twoWays:Boolean=false;
		
		public function clone():FeatureRule{
			var newRule:FeatureRule = new FeatureRule();
			newRule.created = this.created;
			newRule.enabled = this.enabled;
			newRule.id = this.id;
			newRule.updated = this.updated;
			newRule.name = this.name;
			newRule.adminOrder = this.adminOrder;
			newRule.twoWays = this.twoWays;
			newRule.featureId = this.featureId;
			newRule.domain = this.domain;
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
			newRule.properties = new ArrayCollection();
			if(this.properties!=null){
				for each(var property:Object in this.properties){
					newRule.properties.addItem((property as FeatureRuleProperty).clone());
				}
			}
			return newRule;
		}
		
	}
}
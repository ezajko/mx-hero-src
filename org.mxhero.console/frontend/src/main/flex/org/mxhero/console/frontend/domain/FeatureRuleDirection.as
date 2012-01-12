package org.mxhero.console.frontend.domain
{
	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.FeatureRuleDirectionVO")]
	public class FeatureRuleDirection
	{
		public static const DOMAIN:String="domain";
		public static const GROUP:String="group";
		public static const INDIVIDUAL:String="individual";
		public static const ANYONE:String="anyone";
		public static const ANYONEELSE:String="anyoneelse";
		public static const ALLDOMAINS:String="alldomains";
		public static const OWNDOMAIN:String="owndomain";
		
		public var id:Number;
		public var directionType:String;
		public var freeValue:String;
		public var domain:String;
		public var account:String;
		public var group:String;
		
		public function clone():FeatureRuleDirection{
			var clonedDirection:FeatureRuleDirection = new FeatureRuleDirection();
			
			clonedDirection.directionType=this.directionType;
			clonedDirection.freeValue=this.freeValue;
			clonedDirection.id=this.id;
			clonedDirection.domain=this.domain;
			clonedDirection.account=this.account;
			clonedDirection.group=this.group;
			
			return clonedDirection;
		}
	}
}
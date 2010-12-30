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
		
		public var id:Number;
		public var directionType:String;
		public var freeValue:String;
		public var valueId:Number;
		
		public function clone():FeatureRuleDirection{
			var clonedDirection:FeatureRuleDirection = new FeatureRuleDirection();
			
			clonedDirection.directionType=this.directionType;
			clonedDirection.freeValue=this.freeValue;
			clonedDirection.id=this.id;
			clonedDirection.valueId=this.valueId;
			
			return clonedDirection;
		}
	}
}
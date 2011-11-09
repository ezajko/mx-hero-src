package org.mxhero.console.commons.feature
{
	
	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.FeatureRulePropertyVO")]
	public class FeatureRuleProperty
	{

		public var id:Number;
		public var propertyKey:String;
		public var propertyValue:String;
		
		public function clone():FeatureRuleProperty{
			var clonedProperty:FeatureRuleProperty = new FeatureRuleProperty();
			clonedProperty.id=this.id;
			clonedProperty.propertyKey=this.propertyKey;
			clonedProperty.propertyValue=this.propertyValue;
			return clonedProperty;
		}
	}
}
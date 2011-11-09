package org.mxhero.console.commons.feature
{
	import mx.collections.ArrayCollection;

	public class PropertyHelper
	{

		public static function getProperty(data:ArrayCollection, key:String):FeatureRuleProperty{
			for each(var property:FeatureRuleProperty in data){
				if(property.propertyKey==key){
					return property;
				}
			}
			return null;
		}
	
		public static function getProperties(data:ArrayCollection, key:String):ArrayCollection{
			var properties:ArrayCollection=new ArrayCollection();
			
			for each(var property:FeatureRuleProperty in data){
				if(property.propertyKey==key){
					properties.addItem(property);
				}
			}
			if(properties.length>0){
				return properties;
			}
			return null;
		}
	}
}
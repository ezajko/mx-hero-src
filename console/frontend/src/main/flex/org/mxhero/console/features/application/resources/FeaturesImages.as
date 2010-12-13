package org.mxhero.console.features.application.resources
{
	public class FeaturesImages
	{
		[Embed(source="/images/features/disabled.png")] 
		[Bindable]
		public static var DISABLED:Class; 
		
		[Embed(source="/images/features/enabled.png")] 
		[Bindable]
		public static var ENABLED:Class; 
		
		[Embed(source="/images/features/managed.png")] 
		[Bindable]
		public static var MANAGED:Class; 
	}
}
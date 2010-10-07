package org.mxhero.console.frontend.infrastructure
{
	public class Resources
	{
		[Embed(source="images/alert.png")] 
		[Bindable]
		public static var ALERT_ICON:Class; 
		
		[Embed(source="images/check.png")] 
		[Bindable]
		public static var CHECK_ICON:Class; 
	}
}
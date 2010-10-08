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
		
		[Embed(source="images/logout.png")] 
		[Bindable]
		public static var LOGOUT_ICON:Class; 
		
		[Embed(source="images/logo.png")] 
		[Bindable]
		public static var LOGO_IMG:Class; 
	}
}
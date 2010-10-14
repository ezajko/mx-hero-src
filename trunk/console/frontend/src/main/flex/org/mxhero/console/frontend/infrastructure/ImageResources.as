package org.mxhero.console.frontend.infrastructure
{
	public class ImageResources
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
		
		[Embed(source="images/configurations_ico.png")] 
		[Bindable]
		public static var CONFIGURATIONS_ICO:Class; 
		
		[Embed(source="images/policies_ico.png")] 
		[Bindable]
		public static var POLICIES_ICO:Class; 
		
		[Embed(source="images/reports_ico.png")] 
		[Bindable]
		public static var REPORTS_ICO:Class; 
	}
}
package org.mxhero.console.commons.component.mails
{
	public class CommonsImages
	{
		[Embed(source="/images/reports/fromto/delivered.png")] 
		[Bindable]
		public static var DELIVERED:Class; 
		
		[Embed(source="/images/reports/fromto/redirected.png")] 
		[Bindable]
		public static var REDIRECTED:Class; 
		
		[Embed(source="/images/reports/fromto/dropped.png")] 
		[Bindable]
		public static var DROPPED:Class; 
	}
}
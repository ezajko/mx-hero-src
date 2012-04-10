package org.mxhero.console.mails
{
	public class CommonsImages
	{
		[Embed(source="/images/component/delivered.png")] 
		[Bindable]
		public static var DELIVERED:Class; 
		
		[Embed(source="/images/component/redirected.png")] 
		[Bindable]
		public static var REDIRECT:Class; 
		
		[Embed(source="/images/component/dropped.png")] 
		[Bindable]
		public static var DROPPED:Class; 
	}
}
package org.mxhero.console.commons.resources
{
	public class SoundResources
	{
		[Embed(source="/sounds/startup.mp3")] 
		[Bindable]
		public static var STARTUP:Class; 
		
		[Embed(source="/sounds/error.mp3")] 
		[Bindable]
		public static var ERROR:Class; 
		
		[Embed(source="/sounds/message.mp3")] 
		[Bindable]
		public static var MESSAGE:Class; 
	}
}
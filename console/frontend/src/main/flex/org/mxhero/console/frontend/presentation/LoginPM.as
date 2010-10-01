package org.mxhero.console.frontend.presentation
{
	import mx.controls.Alert;

	[Landmark(name="main.login")]
	public class LoginPM
	{	
		[Enter(time="first")]
		public function firstEnter():void
		{
			Alert.show("main.login:First");
			
		}
		
		[Enter(time="next")]
		public function enter():void
		{
			Alert.show("main.login:Next");
		}
		
		[Exit]
		public function exit():void
		{
			Alert.show("main.login:Exit");
		}
	}
}
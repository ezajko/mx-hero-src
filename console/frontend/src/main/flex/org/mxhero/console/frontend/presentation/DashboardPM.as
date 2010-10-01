package org.mxhero.console.frontend.presentation
{
	import mx.controls.Alert;
	import org.spicefactory.parsley.core.messaging.MessageProcessor;

	[Landmark(name="main.dashboard")]
	public class DashboardPM
	{

		[Enter(time="first")]
		public function firstEnter():void
		{
			Alert.show("main.dashboard:First");
			
		}
		
		[Enter(time="next")]
		public function enter():void
		{
			Alert.show("main.dashboard:Next");
		}
		
		[Exit]
		public function exit():void
		{
			Alert.show("main.dashboard:Exit");
		}
	}
}
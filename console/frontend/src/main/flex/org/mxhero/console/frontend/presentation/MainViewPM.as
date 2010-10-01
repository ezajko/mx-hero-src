package org.mxhero.console.frontend.presentation
{
	import com.adobe.cairngorm.navigation.NavigationEvent;
	
	import mx.controls.Alert;
	
	[Landmark(name="main")]
	public class MainViewPM
	{	
		[MessageDispatcher]
		public var dispatcher:Function
		
		public function navigateTo(destination:String):void
		{
			dispatcher(NavigationEvent.createNavigateToEvent(destination));
		}
		
		[Enter(time="first")]
		public function firstEnter():void
		{
			Alert.show("main:First");
			
		}
		
		[Enter(time="next")]
		public function enter():void
		{
			Alert.show("main:Next");
		}
		
		[Exit]
		public function exit():void
		{
			Alert.show("main:Exit");
		}
	}
}
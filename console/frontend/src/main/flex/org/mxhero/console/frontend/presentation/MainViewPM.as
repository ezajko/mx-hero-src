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
		
	}
}
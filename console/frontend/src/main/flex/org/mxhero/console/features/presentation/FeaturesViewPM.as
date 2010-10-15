package org.mxhero.console.features.presentation
{
	import com.adobe.cairngorm.navigation.NavigationEvent;
	
	import mx.controls.Alert;
	import mx.core.Container;

	[Landmark(name="main.dashboard.features")]
	public class FeaturesViewPM
	{
		[MessageDispatcher]
		public var dispatcher:Function;
		
		private function navigateTo(destination:String):void
		{
			dispatcher(NavigationEvent.createNavigateToEvent(destination));
		}
	}
}
package org.mxhero.console.reports.presentation
{
	import com.adobe.cairngorm.navigation.NavigationEvent;
	
	import mx.controls.Alert;
	import mx.core.Container;

	[Landmark(name="main.dashboard.reports")]
	public class ReportsViewPM
	{
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Bindable]
		public static var container:Container;
		
		[Enter(time="first")]
		public function enter():void
		{
			container.createComponentsFromDescriptors();
		}
		
		private function navigateTo(destination:String):void
		{
			dispatcher(NavigationEvent.createNavigateToEvent(destination));
		}
	}
}
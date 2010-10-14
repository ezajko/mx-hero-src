package org.mxhero.console.configurations.presentation
{
	import com.adobe.cairngorm.navigation.NavigationEvent;
	
	import mx.controls.Alert;
	import mx.core.Container;

	[Landmark(name="main.dashboard.configurations")]
	public class ConfigurationsViewPM
	{
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Bindable]
		public static var container:Container;
		
		[Enter(time="first")]
		public function enter():void
		{
			Alert.show("enter to configurations first");
			container.createComponentsFromDescriptors();
		}
		
		private function navigateTo(destination:String):void
		{
			dispatcher(NavigationEvent.createNavigateToEvent(destination));
		}
	}
}
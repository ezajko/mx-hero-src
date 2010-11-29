package org.mxhero.console.configurations.presentation
{
	import com.adobe.cairngorm.navigation.NavigationEvent;
	
	import mx.core.Container;
	
	import org.mxhero.console.configurations.application.ConfigurationsDestinations;

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
			container.createComponentsFromDescriptors();
		}
	
		public function navigateTo(destination:String):void
		{
			dispatcher(NavigationEvent.createNavigateToEvent(destination));
		}
	}
}
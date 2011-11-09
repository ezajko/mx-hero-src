package org.mxhero.console.reports.presentation
{
	import com.adobe.cairngorm.navigation.NavigationEvent;
	import com.adobe.cairngorm.navigation.state.ISelectedName;
	
	import mx.core.Container;
	
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.reports.application.ReportsDestinations;

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
			navigateTo(ReportsDestinations.LIST);
		}
		
		public function navigateTo(destination:String):void
		{
			dispatcher(NavigationEvent.createNavigateToEvent(destination));
		}

	}
}
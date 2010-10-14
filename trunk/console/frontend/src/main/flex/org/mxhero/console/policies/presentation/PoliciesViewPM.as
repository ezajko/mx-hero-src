package org.mxhero.console.policies.presentation
{
	import com.adobe.cairngorm.navigation.NavigationEvent;
	
	import mx.controls.Alert;
	import mx.core.Container;

	[Landmark(name="main.dashboard.policies")]
	public class PoliciesViewPM
	{
		[MessageDispatcher]
		public var dispatcher:Function;
		
		private function navigateTo(destination:String):void
		{
			dispatcher(NavigationEvent.createNavigateToEvent(destination));
		}
	}
}
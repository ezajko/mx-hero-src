package org.mxhero.console.frontend.presentation
{
	import com.adobe.cairngorm.navigation.NavigationEvent;
	
	import org.mxhero.console.frontend.application.MainDestination;
	import org.mxhero.console.frontend.application.event.LoadInitialDataEvent;
	import org.mxhero.console.frontend.application.event.LoginEvent;
	
	[Landmark(name="main")]
	public class MainViewPM
	{	
		[MessageDispatcher]
		public var dispatcher:Function
		
		private function navigateTo(destination:String):void
		{
			dispatcher(NavigationEvent.createNavigateToEvent(destination));
		}
		
		[CommandResult]
		public function loginResult (result:*, event:LoginEvent) : void {
			navigateTo(MainDestination.LOADING);
		}
		
		[CommandResult]
		public function loadingResult (result:*, event:LoadInitialDataEvent) : void {
			navigateTo(MainDestination.DASHBOARD);
		}

		[CommandError]
		public function loadingError (fault:*, event:LoadInitialDataEvent) : void {
			navigateTo(MainDestination.LOGIN);
		}
	}
}
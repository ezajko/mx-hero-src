package org.mxhero.console.frontend.presentation
{
	import com.adobe.cairngorm.navigation.NavigationEvent;
	
	import org.mxhero.console.frontend.application.MainDestination;
	import org.mxhero.console.frontend.application.event.LoadInitialDataEvent;
	import org.mxhero.console.frontend.application.event.LoginEvent;
	import org.mxhero.console.frontend.application.message.ViewChangedMessage;
	import org.spicefactory.parsley.core.messaging.MessageProcessor;
	
	[Landmark(name="main")]
	public class MainViewPM
	{	
		[Bindable]
		public var selectedIndex:Number=0;
		
		[MessageDispatcher]
		public var dispatcher:Function
		
		private function navigateTo(destination:String):void
		{
			dispatcher(NavigationEvent.createNavigateToEvent(destination));
		}
		
		[CommandResult]
		public function loginResult (result:*, event:LoginEvent) : void {
			dispatcher(new ViewChangedMessage());
			navigateTo(MainDestination.LOADING);
			
		}
		
		[CommandResult]
		public function loadingResult (fault:*, event:LoadInitialDataEvent) : void {
			navigateTo(MainDestination.DASHBOARD);
		}
		
	}
}
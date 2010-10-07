package org.mxhero.console.frontend.presentation
{
	import com.adobe.cairngorm.navigation.NavigationEvent;
	
	import org.mxhero.console.frontend.application.MainDestination;
	import org.mxhero.console.frontend.application.event.LoadInitialDataEvent;
	import org.mxhero.console.frontend.application.event.LoginEvent;
	import org.spicefactory.parsley.core.messaging.MessageProcessor;
	
	[Landmark(name="main")]
	public class MainViewPM
	{	
		[MessageDispatcher]
		public var dispatcher:Function
		
		[Bindable]
		public var errorMessage:String;
		
		private function navigateTo(destination:String):void
		{
			dispatcher(NavigationEvent.createNavigateToEvent(destination));
		}
		
		[CommandResult]
		public function loginResult (result:*, event:LoginEvent) : void {
			navigateTo(MainDestination.LOADING);
		}
		
		[CommandResult]
		public function loadingResult (fault:*, event:LoadInitialDataEvent) : void {
			navigateTo(MainDestination.DASHBOARD);
		}

		[MessageError]
		public function handleError (processor:MessageProcessor, error:Error) : void{
			errorMessage=error.message;
		}
		
		[CommandError]
		public function handleError2(fault:*,event:*):void{
			errorMessage="error";
		} 
	}
}
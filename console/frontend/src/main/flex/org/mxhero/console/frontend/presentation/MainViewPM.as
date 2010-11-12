package org.mxhero.console.frontend.presentation
{
	import com.adobe.cairngorm.navigation.NavigationEvent;
	
	import mx.controls.Alert;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	
	import org.mxhero.console.frontend.application.MainDestination;
	import org.mxhero.console.frontend.application.event.LoadInitialDataEvent;
	import org.mxhero.console.frontend.application.event.LoginEvent;
	import org.mxhero.console.frontend.application.event.RecoverPasswordEvent;
	import org.mxhero.console.frontend.application.message.ViewChangedMessage;
	import org.mxhero.console.frontend.application.resources.LoginProperties;
	import org.spicefactory.parsley.core.messaging.MessageProcessor;
	
	[Landmark(name="main")]
	public class MainViewPM
	{	
		
		[MessageDispatcher]
		public var dispatcher:Function
		
		[Bindable]
		private var rm:IResourceManager = ResourceManager.getInstance();
		
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
		public function loadingResult (result:*, event:LoadInitialDataEvent) : void {
			navigateTo(MainDestination.DASHBOARD);
		}
		
		[CommandResult]
		public function recoverResult (result:*, event:RecoverPasswordEvent) : void {
			dispatcher(new ViewChangedMessage());
			Alert.show(rm.getString(LoginProperties.NAME,LoginProperties.PASSWORD_SENT_TOMAIL),event.mail,Alert.OK);
		}
	}
}
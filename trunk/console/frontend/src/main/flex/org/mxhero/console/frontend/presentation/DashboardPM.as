package org.mxhero.console.frontend.presentation
{
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	
	import org.mxhero.console.frontend.application.event.LogoutEvent;
	import org.spicefactory.parsley.core.messaging.MessageProcessor;

	[Landmark(name="main.dashboard")]
	public class DashboardPM
	{
		private static const DASHBOARD_RESOURCE:String="dashboard";
		private static const LOGOUT_CONFIRM_TEXT:String="alert.logout.confirm.text";
		
		[Bindable]
		private var rm:IResourceManager = ResourceManager.getInstance();
		
		[MessageDispatcher]
		public var dispatcher:Function;

		[Bindable]
		[CommandStatus(type="org.mxhero.console.frontend.application.event.LogoutEvent")]
		public var isLogingout:Boolean = false;
		
		public function logout():void{
			Alert.show(rm.getString(DASHBOARD_RESOURCE,LOGOUT_CONFIRM_TEXT),"",Alert.YES|Alert.NO,null,logoutHandler);
		}
		
		public function logoutHandler(event:CloseEvent):void{
			if(event.detail==Alert.YES){
				dispatcher(new LogoutEvent());
			}
		}
		
	}

}
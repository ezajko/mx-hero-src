package org.mxhero.console.frontend.presentation
{
	import mx.controls.Alert;
	import mx.rpc.events.FaultEvent;
	
	import org.mxhero.console.frontend.application.event.LoginEvent;
	import org.mxhero.console.frontend.application.event.RecoverPasswordEvent;
	import org.spicefactory.parsley.core.messaging.command.CommandStatus;

	[Landmark(name="main.login")]
	public class LoginPM
	{	
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Bindable]
		public var username:String="";
		[Bindable]
		public var password:String="";
		[Bindable]
		public var mail:String="";
		
		[Bindable]
		[CommandStatus(type="org.mxhero.console.frontend.application.event.LoginEvent")]
		public var isLoging:Boolean = true;
		
		public function login():void
		{
			dispatcher(new LoginEvent(username,password));
		}
		
		public function recoverPassword():void{
			dispatcher(new RecoverPasswordEvent(mail));
		}
		
		[CommandError(type="org.mxhero.console.frontend.application.event.LoginEvent")]
		public function handleResult (fault:FaultEvent, trigger:LoginEvent) : void {
			Alert.show(fault.fault.faultString);
		}
		
		public function clear():void{
			username="";
			password="";
			mail="";
		}
	}
}
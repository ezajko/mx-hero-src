package org.mxhero.console.frontend.presentation
{
	import mx.rpc.events.FaultEvent;
	
	import org.mxhero.console.frontend.application.event.LoadInitialDataEvent;
	import org.mxhero.console.frontend.application.event.LoginEvent;
	import org.mxhero.console.frontend.application.event.RecoverPasswordEvent;

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
		public var isLoging:Boolean = false;
		
		[Bindable]
		[CommandStatus(type="org.mxhero.console.frontend.application.event.RecoverPasswordEvent")]
		public var isRecovering:Boolean = false;
		
		public function login():void
		{
			dispatcher(new LoginEvent(username,password));
		}
		
		public function recoverPassword():void{
			dispatcher(new RecoverPasswordEvent(mail));
		}

		[Exit]
		public function exit():void
		{
			this.password="";
			this.mail="";
		}

	}
}
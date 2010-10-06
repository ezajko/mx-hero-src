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
		public var errorMessage:String="";
		[Bindable]
		public var hasError:Boolean=false;
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
			hasError=false;
			errorMessage="";
			dispatcher(new LoginEvent(username,password));
		}
		
		public function recoverPassword():void{
			dispatcher(new RecoverPasswordEvent(mail));
		}
		
		[CommandError]
		public function loginError (fault:FaultEvent, trigger:LoginEvent) : void {
			this.hasError=true;
			this.errorMessage=fault.fault.faultCode;
		}
		
		[CommandError]
		public function loadingError (fault:FaultEvent, event:LoadInitialDataEvent) : void {
			this.hasError=true;
			this.errorMessage=fault.fault.faultCode;			
		}
		
		[Exit]
		public function exit():void
		{
			this.password="";
			this.errorMessage="";
			this.hasError=false;
			this.mail="";
		}

	}
}
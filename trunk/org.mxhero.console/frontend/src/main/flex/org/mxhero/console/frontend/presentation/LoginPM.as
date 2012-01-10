package org.mxhero.console.frontend.presentation
{
	import mx.rpc.events.FaultEvent;
	
	import org.mxhero.console.frontend.application.event.IsAuthenticadedEvent;
	import org.mxhero.console.frontend.application.event.LoadInitialDataEvent;
	import org.mxhero.console.frontend.application.event.LoginEvent;
	import org.mxhero.console.frontend.application.event.RecoverPasswordEvent;
	import org.mxhero.console.frontend.application.message.ViewChangedMessage;

	[Landmark(name="main.login")]
	public class LoginPM
	{	
		[Bindable]
		[Inject]
		public var mainView:MainViewPM;
		[Bindable]
		public var hasToAuthenticated:Boolean=false;
		
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

		[Enter(time="first")]
		public function enter():void
		{
			hasToAuthenticated=false;
			dispatcher(new IsAuthenticadedEvent());
		}
		
		[Exit]
		public function exit():void
		{
			this.password="";
			this.mail="";
		}

		[CommandResult]
		public function isAuthenticadedResult (result:*, event:IsAuthenticadedEvent) : void {
			if(result==true){
				mainView.loginResult(null,null);
				hasToAuthenticated=false;
			}else{
				hasToAuthenticated=true;
			}
		}
	}
}
/*
* mxHero is a platform that intends to provide a single point of development 
* and single point of distribution for email solutions and enhancements. It does this
* by providing an extensible framework for rapid development and deployment of
* email solutions.
* 
* Copyright (C) 2012  mxHero Inc.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
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
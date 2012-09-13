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
package org.mxhero.console.configurations.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.ChangePasswordEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.infrastructure.AuthorizeHelper;

	public class ChangePasswordCommand
	{
		[Inject(id="applicationUserService")]
		public var service:RemoteObject;
		[Inject]
		public var helper:AuthorizeHelper;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:ChangePasswordEvent):AsyncToken
		{
			if(helper.checkUserForAuthority("ROLE_ADMIN") && event.domainOwner>-1){
				return service.changePassword(event.oldPassword,event.newPassword,event.domainOwner);
			}
			return service.changePassword(event.oldPassword,event.newPassword);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}
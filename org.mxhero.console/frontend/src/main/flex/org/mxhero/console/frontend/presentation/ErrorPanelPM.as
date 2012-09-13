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
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	
	import org.mxhero.console.commons.resources.ErrorsProperties;
	import org.mxhero.console.frontend.application.event.LogoutEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.application.message.ApplicationMessage;
	import org.mxhero.console.frontend.application.message.ViewChangedMessage;
	import org.mxhero.console.frontend.domain.ApplicationContext;

	public class ErrorPanelPM
	{
		
		[Bindable]
		private var rm:IResourceManager=ResourceManager.getInstance();
		
		[Bindable]
		public var hasNewError:Boolean=false;
		
		[Bindable]
		public var errorMessage:String="";
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		
		[MessageHandler]
		public function handleError(message:ApplicationErrorMessage):void{
			errorMessage=null;
			if(!message.isTextual){
				if(message.key!=null){
					//if user is logged, we need to log him out
					if((message.key.toLowerCase()=='client.authentication'||
						message.key.toLowerCase()=='channel.call.failed'||
						message.key.toLowerCase()=='client.error.messagesend')
						&& context.applicationUser!=null){
						Alert.show(rm.getString(ErrorsProperties.NAME,ErrorsProperties.LOGOUT_MESSAGE),"",Alert.OK,null,logoutHandler);
						return;
					}
					errorMessage=rm.getString(ErrorsProperties.NAME,message.key.toLowerCase());
					
				}
			}else{
				errorMessage=message.key;
			}

			if(errorMessage==null){
				errorMessage=rm.getString(ErrorsProperties.NAME,ErrorsProperties.UNEXPECTED_ERROR_START)
					+message.key
					+rm.getString(ErrorsProperties.NAME,ErrorsProperties.UNEXPECTED_ERROR_END);
			}
			hasNewError=true;
		}
		
		public function logoutHandler(event:CloseEvent):void{
			dispatcher(new LogoutEvent);
		}
		
		[MessageHandler]
		public function handleMessage(message:ApplicationMessage):void{
			clear();
		}
		
		[MessageHandler]
		public function handleViewChanged(message:ViewChangedMessage):void{
			clear();
		}
			
		public function clear():void{
			hasNewError = false;
			errorMessage = "";
		}
	}
}
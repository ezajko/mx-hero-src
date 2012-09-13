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
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.application.message.ApplicationMessage;

	public class MessagePanelPM
	{
		[Bindable]
		public var message:String;
		
		[Bindable]
		private var rm:IResourceManager=ResourceManager.getInstance();
		
		[Bindable]
		public var hasNewMessage:Boolean=false;
		
		[MessageHandler]
		public function handleMessage(applicationMessage:ApplicationMessage):void{
			var newMessage:String=applicationMessage.fullMessage;
			if(newMessage==null || newMessage.length==0){
				newMessage=rm.getString(applicationMessage.source,applicationMessage.key);
			}
			if(newMessage!=null && newMessage.length>0){
				message=newMessage;
				hasNewMessage=true;				
			}
		}
		
		[MessageHandler]
		public function handleError(applicationMessage:ApplicationErrorMessage):void{
			clear();
		}

		public function clear():void{
			hasNewMessage = false;
			message = "";
		}
	}
}
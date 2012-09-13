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
package org.mxhero.console.configurations.presentation.user
{
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	
	import org.mxhero.console.configurations.application.ConfigurationsDestinations;
	import org.mxhero.console.configurations.application.event.ChangePasswordEvent;
	import org.mxhero.console.configurations.application.event.EditApplicationUserEvent;
	import org.mxhero.console.configurations.application.resources.UserProperties;
	import org.mxhero.console.configurations.presentation.ConfigurationsViewPM;
	import org.mxhero.console.frontend.application.message.ApplicationMessage;
	import org.mxhero.console.frontend.application.message.LanguageChangedMessage;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.ApplicationUser;

	[Landmark(name="main.dashboard.configurations.user")]
	public class UserViewPM
	{
		[Bindable]
		public var user:ApplicationUser;

		[Bindable]
		private var rm:IResourceManager = ResourceManager.getInstance();
		
		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Inject]
		[Bindable]
		public var parentModel:ConfigurationsViewPM;
		
		[Bindable]
		public var selectedLocaleIndex:Number=-1;
		
		[Bindable]
		public var isUpdating:Boolean = false;
		
		public function goBack():void{
			if(	user.name != context.applicationUser.name ||
				user.lastName != context.applicationUser.lastName ||
				user.notifyEmail != context.applicationUser.notifyEmail ||
				user.soundsEnabled != context.applicationUser.soundsEnabled ||
				user.locale != context.applicationUser.locale){
				Alert.show(rm.getString(UserProperties.NAME,UserProperties.CANCEL_CHANGES_TEXT),"",Alert.OK|Alert.CANCEL,null,cancelHandler);
			} else {
				parentModel.navigateTo(ConfigurationsDestinations.LIST);
			}
		}
		
		public function cancelHandler(event:CloseEvent):void{
			if(event.detail==Alert.OK){
				parentModel.navigateTo(ConfigurationsDestinations.LIST);
			}
		}
		
		[Enter(time="every")]
		public function every():void{
			user = new ApplicationUser();
			user.name = context.applicationUser.name;
			user.lastName = context.applicationUser.lastName;
			user.notifyEmail = context.applicationUser.notifyEmail;
			user.soundsEnabled = context.applicationUser.soundsEnabled;
			user.locale = context.applicationUser.locale;
			for each (var locale:Object in context.locales){
				if(context.applicationUser.locale==locale.toString()){
					selectedLocaleIndex=context.locales.getItemIndex(locale);
				}
			}
		}
		
		public function updateUser(locale:String):void{
			user.locale=locale;
			user.id=context.applicationUser.id;
			dispatcher(new EditApplicationUserEvent(user));
			this.isUpdating=true;
		}
		
		[CommandResult]
		public function updateUserResult(result:*,event:EditApplicationUserEvent):void{
			this.isUpdating=false;
			this.context.applicationUser=result;
			dispatcher(new LanguageChangedMessage(this.context.applicationUser.locale));
			dispatcher(new ApplicationMessage(UserProperties.NAME,UserProperties.UPDATE_USER_RESULT));
		}
		
		[CommandError]
		public function updateUserError(fault:*,event:EditApplicationUserEvent):void{
			this.isUpdating=false;
		}
		
		public function changePassword(oldPassword:String,newPassword:String):void{
			this.isUpdating=true;
			if(context.selectedDomain!=null && context.selectedDomain.owner!=null){
				dispatcher(new ChangePasswordEvent(oldPassword,newPassword,context.selectedDomain.owner.id));
			}else{
				dispatcher(new ChangePasswordEvent(oldPassword,newPassword));
			}
		}
		
		[CommandResult]
		public function changePasswordResult(result:*,event:ChangePasswordEvent):void{
			this.isUpdating=false;
			dispatcher(new ApplicationMessage(UserProperties.NAME,UserProperties.PASSWORD_CHANGED));
		}
		
		[CommandError]
		public function changePasswordError(fault:*,event:ChangePasswordEvent):void{
			this.isUpdating=false;
		}
	}
}
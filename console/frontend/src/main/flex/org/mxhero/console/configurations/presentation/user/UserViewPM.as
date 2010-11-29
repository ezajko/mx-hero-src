package org.mxhero.console.configurations.presentation.user
{
	import mx.controls.Alert;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	
	import org.mxhero.console.configurations.application.ConfigurationsDestinations;
	import org.mxhero.console.configurations.application.event.ChangePasswordEvent;
	import org.mxhero.console.configurations.application.event.EditApplicationUserEvent;
	import org.mxhero.console.configurations.application.resources.UserProperties;
	import org.mxhero.console.configurations.presentation.ConfigurationsViewPM;
	import org.mxhero.console.frontend.application.message.ApplicationMessage;
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
			parentModel.navigateTo(ConfigurationsDestinations.LIST);
		}
		
		[Enter(time="every")]
		public function every():void{
			user = new ApplicationUser();
			user.name = context.applicationUser.name;
			user.lastName = context.applicationUser.lastName;
			user.notifyEmail = context.applicationUser.notifyEmail;
			user.soundsEnabled = context.applicationUser.soundsEnabled;
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
			dispatcher(new ApplicationMessage(UserProperties.NAME,UserProperties.UPDATE_USER_RESULT));
		}
		
		[CommandError]
		public function updateUserError(fault:*,event:EditApplicationUserEvent):void{
			this.isUpdating=false;
		}
		
		public function changePassword(oldPassword:String,newPassword:String):void{
			this.isUpdating=true;
			dispatcher(new ChangePasswordEvent(oldPassword,newPassword));
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
package org.mxhero.console.configurations.application.event
{
	public class ChangePasswordEvent
	{
		public var oldPassword:String;
		
		public var newPassword:String;
		
		public function ChangePasswordEvent(oldPassword:String,newPassword:String)
		{
			this.oldPassword=oldPassword;
			this.newPassword=newPassword;
		}
	}
}
package org.mxhero.console.configurations.application.event
{
	public class ChangePasswordEvent
	{
		public var oldPassword:String;
		
		public var newPassword:String;
		
		public var domainOwner:Number;
		
		public function ChangePasswordEvent(oldPassword:String,newPassword:String,domainOwner:Number=-1)
		{
			this.oldPassword=oldPassword;
			this.newPassword=newPassword;
			this.domainOwner=domainOwner;
		}
	}
}
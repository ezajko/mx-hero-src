package org.mxhero.console.frontend.domain
{
	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.EmailAccountVO")]
	public class EmailAccount
	{
		public var id:Number;
		public var name:String;
		public var lastName:String;
		public var account:String;
		public var createdDate:Date;
		public var updatedDate:Date;
		public var groupId:Number;
		
		public function clone():EmailAccount{
			var clonedEmailAccount:EmailAccount = new EmailAccount();
			clonedEmailAccount.createdDate=this.createdDate;
			clonedEmailAccount.account=this.account;
			clonedEmailAccount.id=this.id;
			clonedEmailAccount.lastName=this.lastName;
			clonedEmailAccount.name=this.name;
			clonedEmailAccount.updatedDate=this.updatedDate;
			clonedEmailAccount.groupId=this.groupId;
			return clonedEmailAccount;
		}
	}
}
package org.mxhero.console.frontend.domain
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.EmailAccountVO")]
	public class EmailAccount
	{
		public static const MANUAL:String ="manual";
		
		public var account:String;
		public var createdDate:Date;
		public var updatedDate:Date;
		public var group:String;
		public var domain:String;
		public var aliases:ArrayCollection;
		public var dataSource:String;
		
		public function clone():EmailAccount{
			var clonedEmailAccount:EmailAccount = new EmailAccount();
			clonedEmailAccount.createdDate=this.createdDate;
			clonedEmailAccount.account=this.account;
			clonedEmailAccount.updatedDate=this.updatedDate;
			clonedEmailAccount.group=this.group;
			clonedEmailAccount.domain=this.domain;
			clonedEmailAccount.aliases=new ArrayCollection();
			clonedEmailAccount.dataSource=this.dataSource;
			for each(var alias:Object in this.aliases){
				clonedEmailAccount.aliases.addItem((alias as EmailAccountAlias).clone());
			}
			return clonedEmailAccount;
		}
	}
}
package org.mxhero.console.frontend.domain
{
	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.EmailAccountAliasVO")]
	public class EmailAccountAlias
	{
		public static const MANUAL:String ="manual";
		
		public var name:String;
		
		public var domain:String;
		
		public var dataSource:String;
		
		public var account:EmailAccount;
		
		public function clone():EmailAccountAlias{
			var clonedAlias:EmailAccountAlias = new EmailAccountAlias();
			
			clonedAlias.name=this.name;
			clonedAlias.domain=this.domain;
			clonedAlias.dataSource=this.dataSource;
			clonedAlias.account=this.account;
			
			return clonedAlias;
		}
	}
}
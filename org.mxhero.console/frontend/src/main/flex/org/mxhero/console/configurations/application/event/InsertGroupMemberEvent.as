package org.mxhero.console.configurations.application.event
{
	import mx.collections.ArrayCollection;
	
	import org.mxhero.console.frontend.domain.EmailAccount;
	import org.mxhero.console.frontend.domain.Group;

	public class InsertGroupMemberEvent
	{
		public var group:Group;
		public var accounts:ArrayCollection;
		
		
		public function InsertGroupMemberEvent(group:Group,accounts:ArrayCollection)
		{
			this.accounts=accounts;
			this.group=group;
		}
	}
}
package org.mxhero.console.configurations.application.event
{
	import mx.collections.ArrayCollection;
	
	import org.mxhero.console.frontend.domain.EmailAccount;
	import org.mxhero.console.frontend.domain.Group;

	public class RemoveGroupMemberEvent
	{
		public var accounts:ArrayCollection;
		
		
		public function RemoveGroupMemberEvent(accounts:ArrayCollection)
		{
			this.accounts=accounts;
		}
	}
}
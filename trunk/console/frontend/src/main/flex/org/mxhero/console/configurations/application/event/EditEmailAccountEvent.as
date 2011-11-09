package org.mxhero.console.configurations.application.event
{
	import org.mxhero.console.frontend.domain.EmailAccount;

	public class EditEmailAccountEvent
	{
		public var account:EmailAccount;
		
		public function EditEmailAccountEvent(account:EmailAccount)
		{
			this.account=account;
		}
	}
}
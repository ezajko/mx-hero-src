package org.mxhero.console.configurations.application.event
{
	import org.mxhero.console.frontend.domain.EmailAccount;

	public class InsertEmailAccountEvent
	{
		public var emailAccount:EmailAccount;
		
		public var domainId:String;
		
		public function InsertEmailAccountEvent(domainId:String,emailAccount:EmailAccount)
		{
			this.domainId=domainId;
			this.emailAccount=emailAccount;
		}
	}
}
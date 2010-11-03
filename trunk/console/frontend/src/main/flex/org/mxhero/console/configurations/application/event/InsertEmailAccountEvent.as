package org.mxhero.console.configurations.application.event
{
	import org.mxhero.console.frontend.domain.EmailAccount;

	public class InsertEmailAccountEvent
	{
		public var emailAccount:EmailAccount;
		
		public var domainId:Number;
		
		public function InsertEmailAccountEvent(domainId:Number,emailAccount:EmailAccount)
		{
			this.domainId=domainId;
			this.emailAccount=emailAccount;
		}
	}
}
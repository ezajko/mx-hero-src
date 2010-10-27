package org.mxhero.console.configurations.application.event
{

	import org.mxhero.console.frontend.domain.Domain;

	public class InsertDomainEvent
	{
		public var domain:Domain;
		
		public var hasOwner:Boolean;
		
		public var password:String;
		
		public var email:String;
		
		public function InsertDomainEvent(domain:Domain,hasOwner:Boolean,password:String,email:String)
		{
			this.domain=domain;
			this.hasOwner=hasOwner;
			this.password=password;
			this.email=email;
		}
	}
}
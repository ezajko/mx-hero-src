package org.mxhero.console.configurations.application.event
{
	public class SaveQuarantineEvent
	{
		public var domain:String;
		
		public var email:String;
		
		public function SaveQuarantineEvent(domain:String,email:String)
		{
			this.domain=domain;
			this.email=email;
		}
	}
}
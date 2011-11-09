package org.mxhero.console.configurations.application.event
{
	public class RemoveDomainEvent
	{
		public var domainId:String;
		
		public function RemoveDomainEvent(domainId:String)
		{
			this.domainId=domainId;
		}
	}
}
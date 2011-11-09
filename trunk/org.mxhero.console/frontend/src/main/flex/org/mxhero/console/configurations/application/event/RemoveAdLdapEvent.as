package org.mxhero.console.configurations.application.event
{
	public class RemoveAdLdapEvent
	{
		public var domainId:String;
		
		public function RemoveAdLdapEvent(domainId:String)
		{
			this.domainId=domainId;
		}
	}
}
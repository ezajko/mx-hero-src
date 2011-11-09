package org.mxhero.console.configurations.application.event
{
	public class RefreshAdLdapEvent
	{
		public var domainId:String;
		
		public function RefreshAdLdapEvent(domainId:String)
		{
			this.domainId=domainId;
		}
	}
}
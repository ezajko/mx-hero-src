package org.mxhero.console.configurations.application.event
{
	import org.mxhero.console.frontend.domain.DomainAdLdap;

	public class InsertAdLdapEvent
	{
		public var adLdap:DomainAdLdap;
		
		public function InsertAdLdapEvent(adLdap:DomainAdLdap)
		{
			this.adLdap=adLdap;
		}
	}
}
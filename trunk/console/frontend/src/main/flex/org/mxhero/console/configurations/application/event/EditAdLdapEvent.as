package org.mxhero.console.configurations.application.event
{
	import org.mxhero.console.frontend.domain.DomainAdLdap;

	public class EditAdLdapEvent
	{
		public var adLdap:DomainAdLdap;
		
		public function EditAdLdapEvent(adLdap:DomainAdLdap)
		{
			this.adLdap=adLdap;
		}
	}
}
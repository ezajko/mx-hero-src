package org.mxhero.console.configurations.application.event
{
	import org.mxhero.console.frontend.domain.DomainAdLdap;

	public class TestAdldapEvent
	{
		public var adLdap:DomainAdLdap;
		
		public function TestAdldapEvent(adLdap:DomainAdLdap)
		{
			this.adLdap=adLdap;
		}
	}
}
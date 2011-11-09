package org.mxhero.console.configurations.application.event
{
	import org.mxhero.console.frontend.domain.Configuration;

	public class TestEmailEvent
	{
		public var config:Configuration;
		
		public function TestEmailEvent(config:Configuration)
		{
			this.config=config;
		}
	}
}
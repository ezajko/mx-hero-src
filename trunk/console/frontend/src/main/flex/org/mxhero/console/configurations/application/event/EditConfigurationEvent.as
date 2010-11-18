package org.mxhero.console.configurations.application.event
{
	import org.mxhero.console.frontend.domain.Configuration;

	public class EditConfigurationEvent
	{
		public var configuration:Configuration;
		
		public function EditConfigurationEvent(configuration:Configuration)
		{
			this.configuration=configuration;
		}
	}
}
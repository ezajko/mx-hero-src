package org.mxhero.console.configurations.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.configurations.application.event.EditConfigurationEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;

	public class EditConfigurationCommand
	{
		[Inject(id="configurationService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:EditConfigurationEvent):AsyncToken
		{
			return service.edit(event.configuration);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}
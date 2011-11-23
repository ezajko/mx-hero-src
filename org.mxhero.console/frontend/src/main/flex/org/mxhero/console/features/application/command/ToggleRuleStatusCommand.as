package org.mxhero.console.features.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.features.application.event.RemoveRuleEvent;
	import org.mxhero.console.features.application.event.ToggleRuleStatusEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;

	public class ToggleRuleStatusCommand
	{
		[Inject(id="ruleService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:ToggleRuleStatusEvent):AsyncToken
		{
			return service.toggleStatus(event.ruleId);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}
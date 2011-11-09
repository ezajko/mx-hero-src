package org.mxhero.console.features.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.features.application.event.CreateNoDomainRuleEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;

	public class CreateNoDomainRuleCommand
	{
		[Inject(id="ruleService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:CreateNoDomainRuleEvent):AsyncToken
		{
			return service.createRule(event.rule, event.featureId);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}
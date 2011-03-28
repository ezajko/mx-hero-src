package org.mxhero.console.reports.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.reports.application.event.GetSpamHitsEvent;

	public class GetSpamHitsCommand
	{
		[Inject(id="threatsReportService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:GetSpamHitsEvent):AsyncToken
		{
			return service.getSpamHits(event.domain,event.since);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}
package org.mxhero.console.reports.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.reports.application.event.GetVirusEmailsEvent;

	public class GetVirusEmailsCommand
	{
		[Inject(id="threatsReportService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:GetVirusEmailsEvent):AsyncToken
		{
			return service.getVirusEmails(event.domain,event.since.getTime(),event.until.getTime());
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}
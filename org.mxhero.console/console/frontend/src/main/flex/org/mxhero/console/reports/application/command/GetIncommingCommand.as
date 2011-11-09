package org.mxhero.console.reports.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.commons.infrastructure.TimeUtils;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.reports.application.event.GetIncommingEvent;

	public class GetIncommingCommand
	{
		[Inject(id="trafficReportService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:GetIncommingEvent):AsyncToken
		{
			return service.getIncomming(event.domainId,event.since.getTime()
				,TimeUtils.offsetToTZ(event.since.getTimezoneOffset()));
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}
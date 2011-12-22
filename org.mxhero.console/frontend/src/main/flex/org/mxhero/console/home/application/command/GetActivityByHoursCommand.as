package org.mxhero.console.home.application.command
{	
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.home.application.event.GetActivityByHoursEvent;
	
	public class GetActivityByHoursCommand
	{
		[Inject(id="homeReportService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:GetActivityByHoursEvent):AsyncToken
		{
			return service.getActivityByHour(event.since.getTime(),event.domainId);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}
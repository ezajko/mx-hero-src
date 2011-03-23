package org.mxhero.console.reports.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.reports.application.event.GetTopTenOutgoingRecipientsByDayEvent;

	public class GetTopTenOutgoingRecipientsByDayCommand
	{
		[Inject(id="trafficReportService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:GetTopTenOutgoingRecipientsByDayEvent):AsyncToken
		{
			return service.getTopTenOutgoingRecipientsByDay(event.domainId,event.day);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}
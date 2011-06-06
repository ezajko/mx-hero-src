package org.mxhero.console.reports.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.reports.application.event.GetTopTenIncommingSendersByDayEvent;

	public class GetTopTenIncommingSendersByDayCommand
	{
		[Inject(id="trafficReportService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:GetTopTenIncommingSendersByDayEvent):AsyncToken
		{
			return service.getTopTenIncomingSendersByDay(event.domainId,event.day.getTime(),event.onlyDomain);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}
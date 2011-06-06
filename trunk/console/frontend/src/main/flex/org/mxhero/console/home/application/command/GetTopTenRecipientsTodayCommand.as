package org.mxhero.console.home.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.home.application.event.GetTopTenRecipientsTodayEvent;
	
	public class GetTopTenRecipientsTodayCommand
	{
		[Inject(id="toptenReportService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:GetTopTenRecipientsTodayEvent):AsyncToken
		{
			var sinceDay:Date = new Date();
			sinceDay.setHours(0,0,0,0);
			return service.getTopTenOutgoingRecipientsByDay(event.domainId,sinceDay.getTime(),true);
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}
package org.mxhero.console.frontend.application.command
{
	
	import mx.collections.ArrayCollection;
	import mx.rpc.AsyncToken;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.event.GetRecordStatsEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	
	public class GetRecordStatsCommand
	{
		[Inject(id="customReportService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:GetRecordStatsEvent):AsyncToken
		{
			return service.getStats(event.insertDate.time, event.sequence);
		}
		
	}
}
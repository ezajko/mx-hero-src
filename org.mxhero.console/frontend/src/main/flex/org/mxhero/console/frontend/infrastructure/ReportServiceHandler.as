package org.mxhero.console.frontend.infrastructure
{
	import mx.messaging.ChannelSet;
	
	import org.mxhero.console.commons.feature.IReport;
	import org.mxhero.console.commons.feature.IReportService;
	import org.mxhero.console.frontend.application.event.ProcessQueryEvent;
	import org.mxhero.console.frontend.domain.ApplicationContext;

	[Bindable]
	public class ReportServiceHandler implements IReportService
	{
		[Inject]
		public var context:ApplicationContext;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		//report param should be IReport, keep it that way until RSL implementation
		public function processQuery(queryId:String, query:String, params:Array, report:Object):void{
			dispatcher(new ProcessQueryEvent(queryId,query,params,report));
		}
		
		public function getDomain():String{
			if(context.selectedDomain!=null){
				return context.selectedDomain.domain;
			}
			return null;
		}
		
		[CommandResult]
		public function getResult(result:*,event:ProcessQueryEvent):void{
			event.report.setResult(event.queryId,result);
		}
		
		public function get applicationChannelSet():ChannelSet{
			return context.defaultChannelApplication;
		}

	}
}
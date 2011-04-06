package org.mxhero.console.frontend.application.event
{
	import org.mxhero.console.commons.feature.IReport;

	public class ProcessQueryEvent
	{
		public var queryId:String;
		public var query:String;
		public var params:Array;
		public var report:IReport;
		
		
		public function ProcessQueryEvent(queryId:String, query:String, params:Array, report:IReport)
		{
			this.queryId=queryId;
			this.query=query;
			this.params=params;
			this.report=report;
		}
	}
}
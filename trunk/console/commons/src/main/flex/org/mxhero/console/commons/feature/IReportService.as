package org.mxhero.console.commons.feature
{
	public interface IReportService
	{
		function queryData(queryId:String, query:String, report:IReport):void;
	}
}
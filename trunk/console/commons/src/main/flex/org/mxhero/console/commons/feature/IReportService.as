package org.mxhero.console.commons.feature
{
	public interface IReportService
	{
		function processQuery(queryId:String, query:String, params:Array, report:IReport):void;
		
		function getDomain():String;
	}
}
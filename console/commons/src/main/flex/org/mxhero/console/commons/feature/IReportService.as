package org.mxhero.console.commons.feature
{
	public interface IReportService
	{
		//report param should be IReport, keep it that way until RSL implementation
		function processQuery(queryId:String, query:String, params:Array, report:Object):void;
		
		function getDomain():String;
	}
}
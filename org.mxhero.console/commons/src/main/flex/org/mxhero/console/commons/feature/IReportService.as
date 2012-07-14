package org.mxhero.console.commons.feature
{
	import mx.messaging.ChannelSet;

	public interface IReportService
	{
		//report param should be IReport, keep it that way until RSL implementation
		function processQuery(queryId:String, query:String, params:Array, report:Object):void;
		
		function getDomain():String;
		
		function get applicationChannelSet():ChannelSet;

	}
}
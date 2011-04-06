package org.mxhero.console.commons.feature
{
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;

	public interface IReport extends IEventDispatcher
	{
		function setResult(queryId:String, result:*):void;
		
		function setReportService(service:IReportService):void;

		function refresh():void;
	}
}
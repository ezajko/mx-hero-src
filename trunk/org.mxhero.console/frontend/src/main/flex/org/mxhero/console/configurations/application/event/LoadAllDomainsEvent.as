package org.mxhero.console.configurations.application.event
{
	public class LoadAllDomainsEvent
	{
		public var domainFilter:String;
		public var pageNumber:Number;
		public var pageSize:Number;
		
		
		public function LoadAllDomainsEvent(domainFilter:String,
											pageNumber:Number,
											pageSize:Number)
		{
			this.domainFilter=domainFilter;
			this.pageNumber=pageNumber;
			this.pageSize=pageSize;
		}
	}
}
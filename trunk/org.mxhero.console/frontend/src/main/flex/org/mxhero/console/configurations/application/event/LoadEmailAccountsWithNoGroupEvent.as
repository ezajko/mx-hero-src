package org.mxhero.console.configurations.application.event
{
	public class LoadEmailAccountsWithNoGroupEvent
	{
		public var domainId:String;
		
		public var pageNo:Number;
		
		public var pageSize:Number;
		
		public function LoadEmailAccountsWithNoGroupEvent(domainId:String,pageNo:Number,pageSize:Number)
		{
			this.domainId=domainId;
			this.pageNo=pageNo;
			this.pageSize=pageSize;
		}
	}
}
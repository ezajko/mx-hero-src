package org.mxhero.console.configurations.application.event
{
	public class LoadAllGroupsEvent
	{
		public var domainId:String;
		public var pageNo:Number;
		public var pageSize:Number;
		
		public function LoadAllGroupsEvent(domainId:String,pageNo:Number,pageSize:Number)
		{
			this.domainId=domainId;
			this.pageNo=pageNo;
			this.pageSize=pageSize;
		}
	}
}
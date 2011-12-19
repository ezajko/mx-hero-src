package org.mxhero.console.configurations.application.event
{
	public class LoadEmailAccountsByGroupEvent
	{
		public var groupName:String;
		public var domainId:String;
		public var pageNo:Number;
		public var pageSize:Number;
		
		public function LoadEmailAccountsByGroupEvent(groupName:String,domainId:String,pageNo:Number,pageSize:Number)
		{
			this.groupName=groupName;
			this.domainId=domainId;
			this.pageNo=pageNo;
			this.pageSize=pageSize;
		}
	}
}
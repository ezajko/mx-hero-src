package org.mxhero.console.configurations.application.event
{
	public class LoadAllEmailAccountsEvent
	{
		public var domainId:String;
		
		public var email:String;
		
		public var groupId:String;
		
		public var pageNo:Number;
		
		public var pageSize:Number;
		
		public function LoadAllEmailAccountsEvent(domainId:String,email:String,groupId:String,pageNo:Number,pageSize:Number)
		{
			this.domainId=domainId;
			this.email=email;
			this.groupId=groupId;
			this.pageNo=pageNo;
			this.pageSize=pageSize;
		}
	}
}
package org.mxhero.console.configurations.application.event
{
	public class LoadAllEmailAccountsEvent
	{
		public var domainId:Number;
		
		public var email:String;
		
		public var name:String;
		
		public var lastName:String;
		
		public var page:Number;
		
		public var pageSize:Number;
		
		public function LoadAllEmailAccountsEvent(domainId:Number,email:String,name:String,lastName:String,page:Number,pageSize:Number)
		{
			this.domainId=domainId;
			this.email=email;
			this.name=name;
			this.lastName=lastName;
			this.page=page;
			this.pageSize=pageSize;
		}
	}
}
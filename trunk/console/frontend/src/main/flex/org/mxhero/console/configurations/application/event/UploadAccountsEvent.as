package org.mxhero.console.configurations.application.event
{
	import mx.collections.ArrayCollection;

	public class UploadAccountsEvent
	{
		public var accounts:ArrayCollection;
		
		public var domainId:String;
		
		public var failOnError:Boolean;
		
		public function UploadAccountsEvent(accounts:ArrayCollection, domainId:String, failOnError:Boolean)
		{
			this.accounts=accounts;
			this.domainId=domainId;
			this.failOnError=failOnError;
		}
	}
}
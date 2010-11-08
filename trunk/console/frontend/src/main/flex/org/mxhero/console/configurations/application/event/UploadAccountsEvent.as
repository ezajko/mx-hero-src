package org.mxhero.console.configurations.application.event
{
	import mx.collections.ArrayCollection;

	public class UploadAccountsEvent
	{
		public var accounts:ArrayCollection;
		
		public var domainId:Number;
		
		public var failOnError:Boolean;
		
		public function UploadAccountsEvent(accounts:ArrayCollection, domainId:Number, failOnError:Boolean)
		{
			this.accounts=accounts;
			this.domainId=domainId;
			this.failOnError=failOnError;
		}
	}
}
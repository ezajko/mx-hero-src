package org.mxhero.console.configurations.application.event
{
	import mx.collections.ArrayCollection;

	public class UploadAccountsEvent
	{
		public var accounts:ArrayCollection;
		
		public var failOnError:Boolean;
		
		public function UploadAccountsEvent(accounts:ArrayCollection, failOnError:Boolean)
		{
			this.accounts=accounts;
			this.failOnError=failOnError;
		}
	}
}
package org.mxhero.console.configurations.application.command
{
	public class InsertDomainCommand
	{
		[Inject(id="domainService")]
		public var service:RemoteObject;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function execute(event:LoadAllDomainsEvent):AsyncToken
		{
			return service.insert();
		}
		
		public function error (fault:Fault) : void {
			dispatcher(new ApplicationErrorMessage(fault.faultCode));
		}
	}
}
package org.mxhero.console.frontend.application.command
{
	import mx.rpc.AsyncToken;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.event.RecoverPasswordEvent;

	public class RecoverPasswordCommand
	{
		[Inject(id="applicationUserService")]
		private var service:RemoteObject;
		
		public function execute(event:RecoverPasswordEvent):AsyncToken
		{
			return null;
		}
		
		public function result(event:RecoverPasswordEvent):void
		{
			
		}

	}
}
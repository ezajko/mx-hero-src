package org.mxhero.console.frontend.application.task
{
	import flash.net.registerClassAlias;
	
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.frontend.application.event.LoadInitialDataEvent;
	import org.mxhero.console.frontend.application.message.LoadingMessage;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.ApplicationUser;
	import org.spicefactory.lib.task.Task;

	public class LoadUserDataTask extends Task
	{
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Inject(id="applicationUserService")]
		[Bindable]
		public var service:RemoteObject;
		
		[Inject]
		[Bindable]
		public var applicationContext:ApplicationContext;
		
		public function LoadUserDataTask()
		{
			super();
		}
		
		protected override function doStart ():void{
			dispatcher(new LoadingMessage("Loading User Data"));
			service.addEventListener(ResultEvent.RESULT,onResult);
			service.addEventListener(FaultEvent.FAULT,onFault);
			service.findByUserName((data as LoadInitialDataEvent).username);
		}
		
		public function onResult(result:ResultEvent):void{
			applicationContext.applicationUser = result.result as ApplicationUser;
			service.removeEventListener(ResultEvent.RESULT,onResult);
			complete();
		}
		
		public function onFault(fault:FaultEvent):void{
			service.removeEventListener(FaultEvent.FAULT,onFault);
			error(fault.fault.faultCode);
		}
		
	}
}
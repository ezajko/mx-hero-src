package org.mxhero.console.frontend.application.task
{
	import flash.net.registerClassAlias;
	
	import mx.collections.ArrayCollection;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import org.mxhero.console.commons.resources.CommonsProperties;
	import org.mxhero.console.frontend.application.event.LoadInitialDataEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.application.message.LanguageChangedMessage;
	import org.mxhero.console.frontend.application.message.LoadingMessage;
	import org.mxhero.console.frontend.application.resources.LoadingProperties;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.ApplicationUser;
	import org.mxhero.console.frontend.domain.Configuration;
	import org.spicefactory.lib.task.Task;
	
	public class LoadConfigurationTask extends Task
	{
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Bindable]
		private var rm:IResourceManager=ResourceManager.getInstance();
		
		[Inject(id="loadConfigurationService")]
		[Bindable]
		public var service:RemoteObject;
		
		[Inject]
		[Bindable]
		public var applicationContext:ApplicationContext;
		
		public function LoadConfigurationTask()
		{
			super();
		}
		
		protected override function doStart ():void{
			service.addEventListener(ResultEvent.RESULT,onResult);
			service.addEventListener(FaultEvent.FAULT,onFault);
			service.find();
		}
		
		public function onResult(result:ResultEvent):void{
			applicationContext.configuration=result.result as Configuration;
			service.removeEventListener(ResultEvent.RESULT,onResult);
			complete();
		}
		
		public function onFault(fault:FaultEvent):void{
			service.removeEventListener(FaultEvent.FAULT,onFault);
			error(fault.fault.faultCode);
			dispatcher(new ApplicationErrorMessage(fault.fault.faultCode));
		}
		
	}

}
package org.mxhero.console.frontend.presentation
{
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	
	import org.mxhero.console.frontend.application.event.LoadInitialDataEvent;
	import org.mxhero.console.frontend.application.message.LoadingMessage;
	import org.mxhero.console.frontend.application.resources.LoadingProperties;

	[Landmark(name="main.loading")]
	public class LoadingPM
	{

		[MessageDispatcher]
		public var dispatcher:Function;
		
		private var rm:IResourceManager = ResourceManager.getInstance();
		
		[Bindable]
		public var currentLabel:String;	
		
		[Enter(time="every")]
		public function enter():void
		{
			currentLabel=rm.getString(LoadingProperties.NAME,LoadingProperties.TITLE_LABEL);
			dispatcher(new LoadInitialDataEvent());
		}
		
		[MessageHandler]
		public function handleLoading (message:LoadingMessage) : void {
			currentLabel=message.label;
		}
	}
}
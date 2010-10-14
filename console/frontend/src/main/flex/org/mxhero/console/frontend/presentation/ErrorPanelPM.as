package org.mxhero.console.frontend.presentation
{
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.application.message.ViewChangedMessage;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.infrastructure.ErrorsProperties;

	public class ErrorPanelPM
	{
		[Inject]
		[Bindable]
		public var applicationContext:ApplicationContext;
		
		[Bindable]
		private var rm:IResourceManager=ResourceManager.getInstance();
		
		[Bindable]
		public var hasNewError:Boolean=false;
		
		[Bindable]
		public var errorMessage:String="";
		
		[MessageHandler]
		public function handleError(message:ApplicationErrorMessage):void{
			errorMessage=null;
			if(message.key!=null){
				errorMessage=rm.getString(ErrorsProperties.NAME,message.key.toLowerCase());
			}
			if(errorMessage==null){
				errorMessage=rm.getString(ErrorsProperties.NAME,ErrorsProperties.UNEXPECTED_ERROR_START)
					+message.key
					+rm.getString(ErrorsProperties.NAME,ErrorsProperties.UNEXPECTED_ERROR_END);
			}
			hasNewError=true;
		}
		
		[MessageHandler]
		public function handleViewChanged(message:ViewChangedMessage):void{
			clear();
		}
			
		public function clear():void{
			hasNewError = false;
			errorMessage = "";
		}
	}
}
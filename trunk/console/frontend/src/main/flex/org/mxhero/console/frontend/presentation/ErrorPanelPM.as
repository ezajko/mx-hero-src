package org.mxhero.console.frontend.presentation
{
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.application.message.ViewChangedMessage;
	import org.mxhero.console.frontend.domain.ApplicationContext;

	public class ErrorPanelPM
	{
		[Inject]
		[Bindable]
		public var applicationContext:ApplicationContext;
		
		[Bindable]
		public var hasNewError:Boolean=false;
		
		[Bindable]
		public var errorMessage:String="";
		
		[MessageHandler]
		public function handleError(message:ApplicationErrorMessage):void{
			errorMessage=message.key;
			hasNewError=true;
		}
		
		[MessageHandler]
		public function handleViewChanged(message:ViewChangedMessage):void{
			clear();
		}
			
		public function clear():void{
			hasNewError = false;
		}
	}
}
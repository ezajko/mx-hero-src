package org.mxhero.console.frontend.presentation
{
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.application.message.ApplicationMessage;

	public class MessagePanelPM
	{
		[Bindable]
		public var message:String;
		
		[Bindable]
		private var rm:IResourceManager=ResourceManager.getInstance();
		
		[Bindable]
		public var hasNewMessage:Boolean=false;
		
		[MessageHandler]
		public function handleMessage(applicationMessage:ApplicationMessage):void{
			var newMessage:String=applicationMessage.fullMessage;
			if(newMessage==null || newMessage.length==0){
				newMessage=rm.getString(applicationMessage.source,applicationMessage.key);
			}
			if(newMessage!=null && newMessage.length>0){
				message=newMessage;
				hasNewMessage=true;				
			}
		}
		
		[MessageHandler]
		public function handleError(applicationMessage:ApplicationErrorMessage):void{
			clear();
		}

		public function clear():void{
			hasNewMessage = false;
			message = "";
		}
	}
}
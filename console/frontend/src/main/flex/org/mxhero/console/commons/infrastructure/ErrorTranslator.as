package org.mxhero.console.commons.infrastructure
{
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	
	import org.mxhero.console.commons.resources.ErrorsProperties;

	public class ErrorTranslator
	{
		[Bindable]
		private static var rm:IResourceManager=ResourceManager.getInstance();
		
		public static function translate(errorCode:String):String{
			var errorMessage:String = null;
			if(errorCode!=null){
				errorMessage=rm.getString(ErrorsProperties.NAME,errorCode.toLowerCase());
			}
			if(errorMessage==null){
				errorMessage=rm.getString(ErrorsProperties.NAME,ErrorsProperties.UNEXPECTED_ERROR_START)
					+errorCode
					+rm.getString(ErrorsProperties.NAME,ErrorsProperties.UNEXPECTED_ERROR_END);
			}
			return errorMessage;

		}
	}
}
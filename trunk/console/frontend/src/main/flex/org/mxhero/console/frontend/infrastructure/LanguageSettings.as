package org.mxhero.console.frontend.infrastructure
{
	import flash.system.Capabilities;
	
	import mx.resources.ResourceManager;
	
	import org.mxhero.console.frontend.application.message.LanguageChangedMessage;

	[Bindable]
	public class LanguageSettings
	{

		private var _localChain:Array=ResourceManager.getInstance().localeChain;
		
		private var _selectedLanguage:String;
		
		private var _defaultLocal:String=ResourceManager.getInstance().localeChain[0];
				
		[Init]
		public function init():void{
			this.selectedLanguage=Capabilities.language;
		}
		
		private function validLocale(value:String):String{
			value=value.toLowerCase();
			for(var i:int=0;i<_localChain.length; i++){
				if(_localChain[i].toLowerCase()==value.toLowerCase()){
					return _localChain[i];
				}
			}
			for(var j:int=0;j<_localChain.length; j++){
				if(_localChain[j].substr(0,2).toLowerCase()==value.toLowerCase()){
					return _localChain[j];
				}
			}
			return null;
		}
		
		
		public function set selectedLanguage(value:String):void{
			var newLocale:String = validLocale(value);
			if(newLocale!=null && newLocale!=_defaultLocal){
				ResourceManager.getInstance().localeChain=[newLocale,_defaultLocal];
				_selectedLanguage=newLocale;
			} else {
				ResourceManager.getInstance().localeChain=[_defaultLocal];
				_selectedLanguage=_defaultLocal;
			}
		}

		public function get defaultLocal():String
		{
			return _defaultLocal;
		}

		public function set defaultLocal(value:String):void
		{
			_defaultLocal=validLocale(value);
			if(_defaultLocal==null){
				_defaultLocal =_localChain[0];
			}
		}

		public function get selectedLanguage():String
		{
			return _selectedLanguage;
		}

		public function get localChain():Array
		{
			return _localChain;
		}
		
		[MessageHandler]
		public function handleLanguageChange(message:LanguageChangedMessage):void{
			this.selectedLanguage=message.locale;
		}
		
	}
}
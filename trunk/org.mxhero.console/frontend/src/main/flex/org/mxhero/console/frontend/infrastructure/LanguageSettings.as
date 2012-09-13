/*
* mxHero is a platform that intends to provide a single point of development 
* and single point of distribution for email solutions and enhancements. It does this
* by providing an extensible framework for rapid development and deployment of
* email solutions.
* 
* Copyright (C) 2012  mxHero Inc.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
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
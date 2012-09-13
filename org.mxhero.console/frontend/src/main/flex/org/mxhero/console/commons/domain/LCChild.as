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
package org.mxhero.console.commons.domain
{
	import flash.events.Event;
	
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;

	[Bindable]
	public class LCChild
	{
		public var key:String;
		
		public var keyDescription:String;
		
		private var rm:IResourceManager=ResourceManager.getInstance();
		
		public var resource:String;
		
		public var requiredAuthority:String;
		
		public var domainExclusive:Boolean=false;
		
		public var adminExclusive:Boolean=false;
		
		public var needsOwner:Boolean=false;
		
		public var navigateTo:String;
		
		public var enabled:Boolean = true;
		
		public function LCChild(){
			rm.addEventListener("change",dispatchChange);
		}
		
		private function dispatchChange(event:Event):void
		{
			dispatchEvent(new Event("change"));
		}
		
		[Bindable("change")]
		public function get label():String{
			return rm.getString(resource,key);
		}
		
		[Bindable("change")]
		public function get description():String{
			return rm.getString(resource,keyDescription);
		}
	}
}
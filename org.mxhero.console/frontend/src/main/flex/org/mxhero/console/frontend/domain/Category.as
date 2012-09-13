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
package org.mxhero.console.frontend.domain
{
	import mx.collections.ArrayCollection;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;

	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.CategoryVO")]
	public class Category
	{
		private var rm:IResourceManager=ResourceManager.getInstance();
		
		public var id:Number;
		private var _label:String;
		public var iconsrc:String;
		public var childs:ArrayCollection;

		
		public function Category(){
			rm.addEventListener("change",dispatchChange);
		}
	
		private function dispatchChange(event:Event):void
		{
			dispatchEvent(new Event("change"));
		}
		
		[Bindable("change")]
		public function get label():String
		{
			var categoryLabel:String = rm.getString("categories",_label);
			if(categoryLabel!=null){
				return categoryLabel;
			}
			return _label;
		}

		public function set label(value:String):void
		{
			_label = value;
		}

	}
}
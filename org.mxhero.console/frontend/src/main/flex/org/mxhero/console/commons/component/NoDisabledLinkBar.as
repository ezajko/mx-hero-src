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
package org.mxhero.console.commons.component
{
	import mx.controls.Button;
	import mx.controls.LinkBar;

	public class NoDisabledLinkBar extends LinkBar
	{
		public function NoDisabledLinkBar()
		{
			super();
		}
		
		override protected function hiliteSelectedNavItem(index:int):void
		{
			var obj: Object = super;
			var child:Button;
			
			// Un-hilite the current selection.
			if (selectedIndex != -1 && selectedIndex < numChildren)
			{
				child = Button(getChildAt(selectedIndex));
				child.enabled = true;
			}
			
			// Set new index.
			obj.selectedIndex = index;
			
			// Hilite the new selection.
			//Uncoment following if you looking for assigning -1 to selectedIndex to manage buttons.
			/*if (index != -1 && index < numChildren)
			{
			child = Button(getChildAt(index));
			child.enabled = false;
			}*/
		}
	}
}
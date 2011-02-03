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
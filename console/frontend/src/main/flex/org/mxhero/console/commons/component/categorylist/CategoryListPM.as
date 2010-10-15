package org.mxhero.console.commons.component.categorylist
{
	public class CategoryListPM
	{
		private var _selectedCategory:Object;
		
		private var _selectedChild:Object;
		
		[Bindable]
		public var title:String;
		[Bindable]
		public var description:String;
		
		public var onClick:Function=null;
		
		[Bindable]
		public function get selectedCategory():Object
		{
			return _selectedCategory;
		}

		public function set selectedCategory(value:Object):void
		{
			_selectedCategory = value;
		}
		
		[Bindable]
		public function get selectedChild():Object
		{
			return _selectedChild;
		}

		public function set selectedChild(value:Object):void
		{
			_selectedChild = value;
		}

		public function clicked():void{
			if(onClick!=null){
				onClick(selectedCategory,selectedChild);
			}
		}
	}
}
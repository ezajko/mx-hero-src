package org.mxhero.console.commons.component.categorylist
{

	public class CategoryListPM
	{
		private var _selectedCategory:Object;
		
		private var _selectedChild:Object;
		
		private static const DEFAULT_COLUMN_WIDTH:Number = 160;
		
		[Bindable]
		public var defaultTitle:String="default";
		
		[Bindable]
		public var defaultDescription:String="default";
		
		[Bindable]
		public var titleLabelFunction:Function=getdefaultTitle;
		
		[Bindable]
		public var descriptionLabelFunction:Function=getdefaultDescription;
		
		[Bindable]
		public var title:String=defaultTitle;

		[Bindable]
		public var description:String=defaultDescription;
		
		[Bindable]
		public var onClick:Function=null;
		
		[Bindable]
		public var columnsWidth:Number=DEFAULT_COLUMN_WIDTH;
		
		[Bindable]
		public function get selectedCategory():Object
		{
			return _selectedCategory;
		}

		public function set selectedCategory(value:Object):void
		{
			if(value==null){
				trace("error");
			}
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
			if(value!=null){
				this.title=titleLabelFunction();
				this.description=descriptionLabelFunction();
			}
		}

		public function clicked():void{
			if(onClick!=null){
				onClick(selectedCategory,selectedChild);
			}
		}

		private function getdefaultTitle():String{
			if(this.selectedCategory!=null && this.selectedChild!=null){
				return this.selectedCategory.label+" > "+this.selectedChild.label;
			} else {
				return defaultTitle;
			}
		}
	
		private function getdefaultDescription():String{
			if(this.selectedChild!=null){
				return this.selectedChild.description;
			} else {
				return defaultDescription;
			}		
		}
	}
}
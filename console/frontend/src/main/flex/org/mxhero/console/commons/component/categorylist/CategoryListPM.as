package org.mxhero.console.commons.component.categorylist
{
	import mx.collections.ArrayCollection;

	public class CategoryListPM
	{
		private var _selectedCategory:Object;
		
		private var _selectedChild:Object;
		
		private static const DEFAULT_COLUMN_WIDTH:Number = 160;
		
		private var _defaultTitle:String="default";
		
		private var _defaultDescription:String="default";
		
		public var categoryWidth:Number=0;
		
		public var categoriesUI:ArrayCollection= new ArrayCollection();
		
		[Bindable]
		public var orderChilds:Boolean=false;
		
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
		public var clickHandler:Function=defaultClickHandler;
		
		public function defaultClickHandler(child:Object):void{
			trace(child);
		}
		
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
			if(value!=null){
				updateLabels();
			}
		}

		private function updateLabels():void{
			this.title=titleLabelFunction();
			this.description=descriptionLabelFunction();
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

		[Bindable]
		public function get defaultTitle():String
		{
			return _defaultTitle;
		}

		public function set defaultTitle(value:String):void
		{
			_defaultTitle = value;
			updateLabels()
		}

		[Bindable]
		public function get defaultDescription():String
		{
			return _defaultDescription;
		}

		public function set defaultDescription(value:String):void
		{
			_defaultDescription = value;
			updateLabels()
		}

		public function getExtendedTitle():String{
			if(this.selectedCategory!=null && this.selectedChild!=null){
				return this.defaultTitle+" > "+this.selectedCategory.label+" > "+this.selectedChild.label;
			} else {
				return defaultTitle;
			}
		}
		
		public function resizeCategories():void
		{
			var hasChanged:Boolean=false;
			for each(var category:Object in categoriesUI){
				category.invalidateDisplayList();
				if(category.width>categoryWidth){
					categoryWidth=category.width;
					hasChanged=true;
				} else {
					category.width=categoryWidth;
				}
				category.invalidateDisplayList();
			}
			if(hasChanged){
				resizeCategories();
			}
		}
	}
}
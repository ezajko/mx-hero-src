package org.mxhero.console.commons.component.categorylist
{
	import flash.display.DisplayObject;
	import flash.display.DisplayObjectContainer;
	
	import mx.collections.ArrayCollection;
	import mx.resources.ResourceManager;

	public class CategoryListPM
	{
		private var _selectedCategory:Object;
		
		private var _selectedChild:Object;
		
		private static const DEFAULT_COLUMN_WIDTH:Number = 160;
		
		private var _defaultTitle:String="default";
		
		private var _defaultDescription:String="default";
		
		[Bindable]
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
		
		
		public function CategoryListPM(){
			ResourceManager.getInstance().addEventListener("change",dispatchChange);
		}
		
		private function dispatchChange(event:Event):void
		{
			dispatchEvent(new Event("change"));
		}
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
		
		private function searchMax():Number{
			var _categoryWidth:Number=0;
			for each(var category:Object in categoriesUI){
				if(category.width>_categoryWidth){
					_categoryWidth=category.width;
				}
			}
			return _categoryWidth;
		}
		
		
		[Bindable("change")]
		public function resizeCategories(event:*=null):void
		{
			this.categoryWidth = searchMax();
			for each(var category:Object in categoriesUI){
				if(this.categoryWidth>0 && this.categoryWidth!=category.width){
					category.width=this.categoryWidth;
					category.invalidateDisplayList();
					category.invalidateSize();
					category.validateSize(true);
				}
			}
		}
	}
}
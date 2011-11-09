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
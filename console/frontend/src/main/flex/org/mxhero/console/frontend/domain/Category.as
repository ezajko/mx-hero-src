package org.mxhero.console.frontend.domain
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.CategoryVO")]
	public class Category
	{
		public var id:Number;
		public var label:String;
		public var iconsrc:String;
		public var childs:ArrayCollection;
	}
}
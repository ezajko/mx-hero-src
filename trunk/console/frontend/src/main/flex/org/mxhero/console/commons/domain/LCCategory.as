package org.mxhero.console.commons.domain
{
	import mx.collections.ArrayCollection;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;

	[Bindable]
	public class LCCategory
	{
		public var key:String;
		
		public var resource:String;
		
		public var iconsrc:*;
		
		public var childs:ArrayCollection;
		
		public var requiredAuthority:String;
		
		public var domainExclusive:Boolean=false;
		
		public var adminExclusive:Boolean=false;
		
		private var rm:IResourceManager=ResourceManager.getInstance();
		
		public function get label():String{
			return rm.getString(resource,key);
		}
	}
}
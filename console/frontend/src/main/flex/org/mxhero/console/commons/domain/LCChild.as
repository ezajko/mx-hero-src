package org.mxhero.console.commons.domain
{
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;

	[Bindable]
	public class LCChild
	{
		public var key:String;
		
		public var keyDescription:String;
		
		private var rm:IResourceManager=ResourceManager.getInstance();
		
		public var resource:String;
		
		public var requiredAuthority:String;
		
		public var navigateTo:String;
		
		public function get label():String{
			return rm.getString(resource,key);
		}
		
		public function get description():String{
			return rm.getString(resource,keyDescription);
		}
	}
}
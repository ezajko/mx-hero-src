package org.mxhero.console.commons.domain
{
	import flash.events.Event;
	
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
		
		public var domainExclusive:Boolean=false;
		
		public var adminExclusive:Boolean=false;
		
		public var needsOwner:Boolean=false;
		
		public var navigateTo:String;
		
		public function LCChild(){
			rm.addEventListener("change",dispatchChange);
		}
		
		private function dispatchChange(event:Event):void
		{
			dispatchEvent(new Event("change"));
		}
		
		[Bindable("change")]
		public function get label():String{
			return rm.getString(resource,key);
		}
		
		[Bindable("change")]
		public function get description():String{
			return rm.getString(resource,keyDescription);
		}
	}
}
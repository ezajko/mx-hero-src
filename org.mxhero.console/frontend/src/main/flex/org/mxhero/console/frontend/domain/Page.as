package org.mxhero.console.frontend.domain
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.PageVO")]
	public class Page
	{
		public var elements:ArrayCollection;
		
		public var totalElements:Number;
		
		public var totalPages:Number;
		
		public var actualPage:Number;
		
		public function hasNext():Boolean{
			return actualPage<totalPages;
		}
		
		public function hasPrev():Boolean{
			return actualPage>1;
		}
	}
}
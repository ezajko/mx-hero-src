package org.mxhero.console.frontend.domain
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.PageVO")]
	public class Page
	{
		public var elements:ArrayCollection;
		
		public var number:Number;
		
		public var numberOfElements:Number;
		
		public var size:Number;
		
		public var totalElements:Number;
		
		public var totalPages:Number;
		
		public var hasNextPage:Boolean;
		
		public var hasPreviousPage:Boolean;
	}
}
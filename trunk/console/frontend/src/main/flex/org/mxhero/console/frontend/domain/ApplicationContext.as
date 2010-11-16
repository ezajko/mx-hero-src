package org.mxhero.console.frontend.domain
{
	import mx.collections.ArrayCollection;

	[Bindable]
	public class ApplicationContext
	{
		public var applicationUser:ApplicationUser;
		
		public var selectedDomain:Domain;
		
		public var groups:ArrayCollection;
		
		public var locales:ArrayCollection;
	}
}
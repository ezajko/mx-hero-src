package org.mxhero.console.frontend.domain
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.ApplicationUserVO")]
	public class ApplicationUser
	{
		public var id:Number ;
		public var name:String;
		public var lastName:String;
		public var notifyEmail:String;
		public var userName:String;
		public var creationDate:Date;
		public var authorities:ArrayCollection;
		public var locale:String;
		public var domain:Domain;

	}
}
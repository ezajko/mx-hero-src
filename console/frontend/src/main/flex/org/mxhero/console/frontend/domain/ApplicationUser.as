package org.mxhero.console.frontend.domain
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.entity.ApplicationUser")]
	public class ApplicationUser
	{
		public var id:Number ;
		public var name:String;
		public var lastName:String;
		public var notifyEmail:String;
		public var password:String;
		public var userName:String;
		public var lastLogin:Date;
		public var lastPasswordUpdate:Date;
		public var creationDate:Date;
		public var validUntil:Date;
		public var enabled:Boolean;
		public var authorities:ArrayCollection;
		public var locale:String;
		public var domain:Domain;
	}
}
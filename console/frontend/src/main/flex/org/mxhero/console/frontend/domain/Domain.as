package org.mxhero.console.frontend.domain
{
	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.DomainVO")]
	public class Domain
	{
		public var id:Number;
		public var domain:String;
		public var server:String;
		public var creationDate:Date;
		public var owner:Owner;
		public var updatedDate:Date;
	}
}
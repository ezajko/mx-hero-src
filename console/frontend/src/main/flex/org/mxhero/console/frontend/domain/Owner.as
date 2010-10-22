package org.mxhero.console.frontend.domain
{
	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.OwnerVO")]
	public class Owner
	{
		public var id:Number
		public var email:String;
	}
}
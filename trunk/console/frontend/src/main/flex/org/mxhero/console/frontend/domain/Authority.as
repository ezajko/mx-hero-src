package org.mxhero.console.frontend.domain
{
	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.entity.Authority")]
	public class Authority
	{
		public var id:Number;
		public var authority:String;
	}
}
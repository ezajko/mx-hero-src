package org.mxhero.console.frontend.domain
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.LdapAccountVO")]
	public class TestAccount
	{
		public var uid:String;
		public var emails:ArrayCollection;
	}
}
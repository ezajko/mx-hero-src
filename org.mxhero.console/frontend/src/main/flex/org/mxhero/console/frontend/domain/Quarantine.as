package org.mxhero.console.frontend.domain
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.QuarantineVO")]
	public class Quarantine
	{
		public var domain:String;
		public var email:String;
	}
}
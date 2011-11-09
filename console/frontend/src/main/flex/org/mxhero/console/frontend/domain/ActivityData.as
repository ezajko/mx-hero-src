package org.mxhero.console.frontend.domain
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.ActivityDataVO")]
	public class ActivityData
	{
		public var incomming:ArrayCollection;
		public var outgoing:ArrayCollection;
		public var spam:ArrayCollection;
		public var blocked:ArrayCollection;
		public var virus:ArrayCollection;
	}
}
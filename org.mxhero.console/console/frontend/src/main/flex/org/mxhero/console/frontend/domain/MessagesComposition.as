package org.mxhero.console.frontend.domain
{
	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.MessagesCompositionVO")]
	public class MessagesComposition
	{
		public var blocked:Number;
		public var spam:Number;
		public var virus:Number;
		public var clean:Number;
	}
}
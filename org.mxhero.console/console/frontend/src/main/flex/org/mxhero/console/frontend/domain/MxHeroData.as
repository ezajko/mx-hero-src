package org.mxhero.console.frontend.domain
{
	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.MxHeroDataVO")]
	public class MxHeroData
	{
		public var domains:Number;
		public var accounts:Number;
		public var groups:Number;
		public var disableRules:Number;
		public var enabledRules:Number;
	}
}
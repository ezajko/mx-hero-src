package org.mxhero.console.frontend.domain
{
	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.FeatureRuleVO")]
	public class FeatureRule
	{
		public var id:Number;
		public var name:String;
		public var created:Date;
		public var updated:Date;
	}
}
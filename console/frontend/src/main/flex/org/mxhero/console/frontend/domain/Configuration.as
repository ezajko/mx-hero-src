package org.mxhero.console.frontend.domain
{
	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.ConfigurationVO")]
	public class Configuration
	{
		public var host:String;
		public var auth:Boolean;
		public var port:Number;
		public var ssl:Boolean;
		public var user:String;
		public var password:String;
		public var adminMail:String;
	}
}
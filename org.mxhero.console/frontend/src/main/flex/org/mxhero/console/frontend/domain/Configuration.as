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
		public var logoPath:String;
		public var newsFeedEnabled:String;
		public var defaultLanguage:String;

		public function clone():Configuration{
			var clonedConfiguration:Configuration=new Configuration();
			
			clonedConfiguration.host=this.host;
			clonedConfiguration.auth=this.auth;
			clonedConfiguration.port=this.port;
			clonedConfiguration.ssl=this.ssl;
			clonedConfiguration.user=this.user;
			clonedConfiguration.password=this.password;
			clonedConfiguration.adminMail=this.adminMail;
			clonedConfiguration.logoPath=this.logoPath;
			clonedConfiguration.newsFeedEnabled=this.newsFeedEnabled;
				
			return clonedConfiguration;
		}
	
	}
}
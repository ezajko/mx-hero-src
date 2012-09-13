/*
* mxHero is a platform that intends to provide a single point of development 
* and single point of distribution for email solutions and enhancements. It does this
* by providing an extensible framework for rapid development and deployment of
* email solutions.
* 
* Copyright (C) 2012  mxHero Inc.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
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
		public var documentationUrl:String;
		public var license:String;

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
			clonedConfiguration.documentationUrl=this.documentationUrl;
			clonedConfiguration.license=this.license;
			
			return clonedConfiguration;
		}
	
	}
}
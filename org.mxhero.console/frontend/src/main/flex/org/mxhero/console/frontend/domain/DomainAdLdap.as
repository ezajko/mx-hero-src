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
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.DomainAdLdapVO")]
	public class DomainAdLdap
	{
		public var domainId:String;
		public var directoryType:String;
		public var addres:String;
		public var port:String;
		public var sslFlag:Boolean;
		public var user:String;
		public var password:String;
		public var filter:String;
		public var base:String;
		public var nextUpdate:Date;
		public var lastUpdate:Date;
		public var error:String;
		public var overrideFlag:Boolean;
		public var dnAuthenticate:String;
		public var accountProperties:ArrayCollection;
		
		public function clone():DomainAdLdap{
			var clonedAdLdap:DomainAdLdap = new DomainAdLdap();
			clonedAdLdap.domainId=this.domainId;
			clonedAdLdap.directoryType=this.directoryType;
			clonedAdLdap.addres=this.addres;
			clonedAdLdap.port=this.port;
			clonedAdLdap.sslFlag=this.sslFlag;
			clonedAdLdap.user=this.user;
			clonedAdLdap.password=this.password;
			clonedAdLdap.filter=this.filter;
			clonedAdLdap.base=this.base;
			clonedAdLdap.nextUpdate=this.nextUpdate;
			clonedAdLdap.lastUpdate=this.lastUpdate;
			clonedAdLdap.error=this.error;
			clonedAdLdap.overrideFlag=this.overrideFlag;
			clonedAdLdap.dnAuthenticate=this.dnAuthenticate;
			if(this.accountProperties!=null){
				clonedAdLdap.accountProperties=new ArrayCollection();
				clonedAdLdap.accountProperties.addAll(this.accountProperties);
			}
			return clonedAdLdap;
		}
	}
}
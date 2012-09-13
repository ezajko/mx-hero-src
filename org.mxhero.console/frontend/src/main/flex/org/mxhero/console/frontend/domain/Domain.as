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
	[RemoteClass(alias="org.mxhero.console.backend.vo.DomainVO")]
	public class Domain
	{
		
		public var domain:String;
		public var server:String;
		public var creationDate:Date;
		public var owner:Owner;
		public var updatedDate:Date;
		public var aliases:ArrayCollection;
		public var adLdap:DomainAdLdap;
		
		public function clone():Domain{
			var clonedDomain:Domain=new Domain();
			clonedDomain.creationDate=this.creationDate;
			clonedDomain.domain=this.domain;
			clonedDomain.server=this.server;
			clonedDomain.updatedDate=this.updatedDate;

			if(this.owner!=null){
				clonedDomain.owner=this.owner.clone();
			}
			if(this.aliases!=null){
				clonedDomain.aliases=new ArrayCollection();
				clonedDomain.aliases.addAll(this.aliases);
			}
			if(this.adLdap!=null){
				clonedDomain.adLdap=this.adLdap.clone();
			}
			return clonedDomain;
		}
	}
}
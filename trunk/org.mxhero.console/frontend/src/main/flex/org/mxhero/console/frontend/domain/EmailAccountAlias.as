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
	[RemoteClass(alias="org.mxhero.console.backend.vo.EmailAccountAliasVO")]
	public class EmailAccountAlias
	{
		public static const MANUAL:String ="manual";
		
		public var name:String;
		
		public var domain:String;
		
		public var dataSource:String;
		
		public var account:EmailAccount;
		
		public function clone():EmailAccountAlias{
			var clonedAlias:EmailAccountAlias = new EmailAccountAlias();
			
			clonedAlias.name=this.name;
			clonedAlias.domain=this.domain;
			clonedAlias.dataSource=this.dataSource;
			clonedAlias.account=this.account;
			
			return clonedAlias;
		}
	}
}
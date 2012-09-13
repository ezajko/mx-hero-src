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
	[RemoteClass(alias="org.mxhero.console.backend.vo.EmailAccountVO")]
	public class EmailAccount
	{
		public static const MANUAL:String ="manual";
		
		public var account:String;
		public var createdDate:Date;
		public var updatedDate:Date;
		public var group:String;
		public var domain:String;
		public var aliases:ArrayCollection;
		public var dataSource:String;
		
		public function clone():EmailAccount{
			var clonedEmailAccount:EmailAccount = new EmailAccount();
			clonedEmailAccount.createdDate=this.createdDate;
			clonedEmailAccount.account=this.account;
			clonedEmailAccount.updatedDate=this.updatedDate;
			clonedEmailAccount.group=this.group;
			clonedEmailAccount.domain=this.domain;
			clonedEmailAccount.aliases=new ArrayCollection();
			clonedEmailAccount.dataSource=this.dataSource;
			for each(var alias:Object in this.aliases){
				clonedEmailAccount.aliases.addItem((alias as EmailAccountAlias).clone());
			}
			return clonedEmailAccount;
		}
	}
}
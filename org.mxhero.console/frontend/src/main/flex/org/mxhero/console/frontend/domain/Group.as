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
	[RemoteClass(alias="org.mxhero.console.backend.vo.GroupVO")]
	public class Group
	{
		public var domain:String;
		public var name:String;
		public var description:String;
		public var createdDate:Date;
		public var updatedDate:Date;
		
		public function clone():Group{
			var clonedGroup:Group = new Group();
			clonedGroup.domain=this.domain;
			clonedGroup.name=this.name;
			clonedGroup.description=this.description;
			clonedGroup.name=this.name;
			clonedGroup.createdDate=this.createdDate;
			clonedGroup.updatedDate=this.updatedDate;
			return clonedGroup;
		}
	}
}
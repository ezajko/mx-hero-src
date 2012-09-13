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
	[RemoteClass(alias="org.mxhero.console.backend.vo.FeatureRuleDirectionVO")]
	public class FeatureRuleDirection
	{
		public static const DOMAIN:String="domain";
		public static const GROUP:String="group";
		public static const INDIVIDUAL:String="individual";
		public static const ANYONE:String="anyone";
		public static const ANYONEELSE:String="anyoneelse";
		public static const ALLDOMAINS:String="alldomains";
		public static const OWNDOMAIN:String="owndomain";
		
		public var id:Number;
		public var directionType:String;
		public var freeValue:String;
		public var domain:String;
		public var account:String;
		public var group:String;
		
		public function clone():FeatureRuleDirection{
			var clonedDirection:FeatureRuleDirection = new FeatureRuleDirection();
			
			clonedDirection.directionType=this.directionType;
			clonedDirection.freeValue=this.freeValue;
			clonedDirection.id=this.id;
			clonedDirection.domain=this.domain;
			clonedDirection.account=this.account;
			clonedDirection.group=this.group;
			
			return clonedDirection;
		}
	}
}
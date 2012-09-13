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
	
	import org.mxhero.console.commons.feature.FeatureRuleProperty;

	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.FeatureRuleVO")]
	public class FeatureRule
	{
		public var id:Number;
		public var name:String;
		public var created:Date;
		public var updated:Date;
		public var enabled:Boolean;
		public var adminOrder:String;
		public var featureId:Number;
		public var domain:String;
		public var fromDirection:FeatureRuleDirection;
		public var toDirection:FeatureRuleDirection;
		public var properties:ArrayCollection;
		public var twoWays:Boolean=false;
		
		public function clone():FeatureRule{
			var newRule:FeatureRule = new FeatureRule();
			newRule.created = this.created;
			newRule.enabled = this.enabled;
			newRule.id = this.id;
			newRule.updated = this.updated;
			newRule.name = this.name;
			newRule.adminOrder = this.adminOrder;
			newRule.twoWays = this.twoWays;
			newRule.featureId = this.featureId;
			newRule.domain = this.domain;
			if(this.fromDirection!=null){
				newRule.fromDirection = this.fromDirection.clone();
			} else {
				newRule.fromDirection = new FeatureRuleDirection();
			}
			if( this.toDirection!=null){
				newRule.toDirection = this.toDirection.clone();
			} else {
				newRule.toDirection = new FeatureRuleDirection();
			}
			newRule.properties = new ArrayCollection();
			if(this.properties!=null){
				for each(var property:Object in this.properties){
					newRule.properties.addItem((property as FeatureRuleProperty).clone());
				}
			}
			return newRule;
		}
		
	}
}
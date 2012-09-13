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
package org.mxhero.console.reports.application.resources
{
	public class ReportsImages
	{
		[Embed(source="/images/reports/export.png")] 
		[Bindable]
		public static var EXPORT:Class; 
		
		[Embed(source="/images/reports/refresh.png")] 
		[Bindable]
		public static var REFRESH:Class; 
		
		[Embed(source="/images/reports/zoom_out.png")] 
		[Bindable]
		public static var ZOOM_OUT:Class; 
		
		[Embed(source="/images/reports/zoom_in.png")] 
		[Bindable]
		public static var ZOOM_IN:Class; 

		
		[Embed(source="/images/reports/queue/close.png")] 
		[Bindable]
		public static var CLOSE:Class; 
		
		[Embed(source="/images/reports/queue/open.png")] 
		[Bindable]
		public static var OPEN:Class; 
		
		[Embed(source="/images/reports/queue/rejected.png")] 
		[Bindable]
		public static var REJECTED:Class; 
		
		[Embed(source="/images/reports/queue/waiting.png")] 
		[Bindable]
		public static var WAITING:Class; 
		
		[Embed(source="/images/reports/queue/delivered.png")] 
		[Bindable]
		public static var DELIVERED:Class; 

		[Embed(source="/images/reports/fromto/dropped.png")] 
		[Bindable]
		public static var DROPPED_FROMTO:Class; 
		
		[Embed(source="/images/reports/fromto/delivered.png")] 
		[Bindable]
		public static var DELIVERED_FROMTO:Class; 
		
		[Embed(source="/images/reports/more.png")] 
		[Bindable]
		public static var MORE:Class; 
	}
}
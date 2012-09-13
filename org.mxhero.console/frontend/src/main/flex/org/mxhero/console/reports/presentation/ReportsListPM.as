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
package org.mxhero.console.reports.presentation
{
	import org.mxhero.console.frontend.application.message.LanguageChangedMessage;
	import org.mxhero.console.frontend.infrastructure.AuthorizeHelper;

	[Landmark(name="main.dashboard.reports.list")]
	public class ReportsListPM
	{
			
		[Inject(id="reportsCategories")]
		[Bindable]
		public var dataSource:Object;
		
		[Inject]
		[Bindable]
		public var reportsViewPM:ReportsViewPM;
		
		[Inject]
		[Bindable]
		public var authorizeHelper:AuthorizeHelper;
		
		[Bindable]
		public var authorizedDataSource:Object=null;
		
		[Enter(time="every")]
		public function every():void{
			authorizedDataSource=authorizeHelper.authorizeList(dataSource);
		}
		
		public function childClickHandler(child:Object,category:Object):void{
			reportsViewPM.navigateTo(child.navigateTo);
		}
		
		[MessageHandler]
		public function handleLanguageChange(message:LanguageChangedMessage):void{
			this.authorizedDataSource=null;
			this.authorizedDataSource=authorizeHelper.authorizeList(dataSource);
		}
	}
}
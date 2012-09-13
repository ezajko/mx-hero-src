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
package org.mxhero.console.frontend.infrastructure
{
	import mx.messaging.ChannelSet;
	
	import org.mxhero.console.commons.feature.IReport;
	import org.mxhero.console.commons.feature.IReportService;
	import org.mxhero.console.frontend.application.event.ProcessQueryEvent;
	import org.mxhero.console.frontend.domain.ApplicationContext;

	[Bindable]
	public class ReportServiceHandler implements IReportService
	{
		[Inject]
		public var context:ApplicationContext;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		//report param should be IReport, keep it that way until RSL implementation
		public function processQuery(queryId:String, query:String, params:Array, report:Object):void{
			dispatcher(new ProcessQueryEvent(queryId,query,params,report));
		}
		
		public function getDomain():String{
			if(context.selectedDomain!=null){
				return context.selectedDomain.domain;
			}
			return null;
		}
		
		[CommandResult]
		public function getResult(result:*,event:ProcessQueryEvent):void{
			event.report.setResult(event.queryId,result);
		}
		
		public function get applicationChannelSet():ChannelSet{
			return context.defaultChannelApplication;
		}

	}
}
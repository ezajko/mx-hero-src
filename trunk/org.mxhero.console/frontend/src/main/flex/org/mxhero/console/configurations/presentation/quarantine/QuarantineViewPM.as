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
package org.mxhero.console.configurations.presentation.quarantine
{
	import org.mxhero.console.configurations.application.ConfigurationsDestinations;
	import org.mxhero.console.configurations.application.event.LoadQuarantineEvent;
	import org.mxhero.console.configurations.application.event.SaveQuarantineEvent;
	import org.mxhero.console.configurations.presentation.ConfigurationsViewPM;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.Quarantine;

	[Landmark(name="main.dashboard.configurations.quarantine")]
	public class QuarantineViewPM
	{
		[MessageDispatcher]
		public var dispatcher:Function;

		[Bindable]
		public var quarantine:Quarantine;
		[Bindable]
		public var hasQuarentine:Boolean=false;
		
		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		
		[Bindable]
		public var isNotUpdating:Boolean=true;
		
		[Inject]
		[Bindable]
		public var parentModel:ConfigurationsViewPM;
		
		[Enter(time="every")]
		public function every():void{
			quarantine=new Quarantine();
			quarantine.domain=context.selectedDomain.domain;
			read();
		}
		
		public function goBack():void{
			parentModel.navigateTo(ConfigurationsDestinations.LIST);
		}
		
		public function save():void{
			dispatcher(new SaveQuarantineEvent(quarantine));
			isNotUpdating=false;
		}
		
		public function read():void{
			dispatcher(new LoadQuarantineEvent(quarantine.domain));
			isNotUpdating=false;
		}
		
		[CommandResult]
		public function saveQuarantineResult(result:*,event:SaveQuarantineEvent):void{
			isNotUpdating=true;
			goBack();
		}
		
		[CommandError]
		public function saveQuarantineError(fault:*,event:SaveQuarantineEvent):void{
			isNotUpdating=true;
			dispatcher(new ApplicationErrorMessage(fault.fault.faultCode));
		}
		
		[CommandResult]
		public function loadQuarantineResult(result:*,event:LoadQuarantineEvent):void{
			isNotUpdating=true;
			if(result!=null){
				this.quarantine=result as Quarantine;
				this.hasQuarentine=true;
			}else{
				this.hasQuarentine=false;
			}
		}
		
		[CommandError]
		public function loadQuarantineError(fault:*,event:LoadQuarantineEvent):void{
			isNotUpdating=true;
 			dispatcher(new ApplicationErrorMessage(fault.fault.faultCode));
		}
	}
}
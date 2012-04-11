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
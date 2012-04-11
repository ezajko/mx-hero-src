package org.mxhero.console.configurations.presentation.quarantine
{
	import org.mxhero.console.configurations.application.ConfigurationsDestinations;
	import org.mxhero.console.configurations.application.event.SaveQuarantineEvent;
	import org.mxhero.console.configurations.presentation.ConfigurationsViewPM;
	import org.mxhero.console.frontend.domain.ApplicationContext;

	[Landmark(name="main.dashboard.configurations.quarantine")]
	public class QuarantineViewPM
	{
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		
		[Bindable]
		public var isNotUpdating:Boolean=true;
		
		[Inject]
		[Bindable]
		public var parentModel:ConfigurationsViewPM;
		
		public function goBack():void{
			parentModel.navigateTo(ConfigurationsDestinations.LIST);
		}
		
		public function save(email:String):void{
			dispatcher(new SaveQuarantineEvent(context.selectedDomain.domain,email));
			isNotUpdating=false;
		}
		
		[CommandResult]
		public function saveQuarantineResult(result:*,event:SaveQuarantineEvent):void{
			isNotUpdating=true;
		}
		
		[CommandError]
		public function saveQuarantineError(fault:*,event:SaveQuarantineEvent):void{
			isNotUpdating=true;
		}
	}
}
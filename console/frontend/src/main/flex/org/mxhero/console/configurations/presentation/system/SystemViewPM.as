package org.mxhero.console.configurations.presentation.system
{
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	
	import org.mxhero.console.configurations.application.ConfigurationsDestinations;
	import org.mxhero.console.configurations.application.event.EditConfigurationEvent;
	import org.mxhero.console.configurations.application.event.LoadConfigurationEvent;
	import org.mxhero.console.configurations.application.event.TestEmailEvent;
	import org.mxhero.console.configurations.application.resources.SystemProperties;
	import org.mxhero.console.configurations.presentation.ConfigurationsViewPM;
	import org.mxhero.console.frontend.application.message.ApplicationMessage;
	import org.mxhero.console.frontend.domain.Configuration;

	[Landmark(name="main.dashboard.configurations.system")]
	public class SystemViewPM
	{
		[Inject]
		[Bindable]
		public var parentModel:ConfigurationsViewPM;
		
		[MessageDispatcher]
		public var dispatcher:Function;

		[Bindable]	
		public var configuration:Configuration;
		
		[Bindable]
		private var rm:IResourceManager = ResourceManager.getInstance();
		
		[Bindable]
		public var isUpdating:Boolean = false;
		
		public function goBack():void{
			parentModel.navigateTo(ConfigurationsDestinations.LIST);
		}
		
		[Enter(time="every")]
		public function every():void{
			dispatcher(new LoadConfigurationEvent());
			isUpdating=true;
		}
		
		[CommandResult]
		public function loadResult(result:*,event:LoadConfigurationEvent):void{
			this.isUpdating=false;
			this.configuration=result;
		}
		
		[CommandError]
		public function loadError(fault:*,event:LoadConfigurationEvent):void{
			this.isUpdating=false;
		}
		
		public function editConfiguration():void{
			dispatcher(new EditConfigurationEvent(this.configuration));
			this.isUpdating=true;
		}

		[CommandResult]
		public function editMailResult(result:*,event:EditConfigurationEvent):void{
			this.isUpdating=false;
			dispatcher(new ApplicationMessage(SystemProperties.NAME,SystemProperties.FORM_UPDATE_MAIL_RESULT));
		}
		
		[CommandError]
		public function editMailError(fault:*,event:EditConfigurationEvent):void{
			this.isUpdating=false;
		}
		
		public function testConfiguration():void{
			dispatcher(new TestEmailEvent(this.configuration));
			this.isUpdating=true;
		}
		
		[CommandResult]
		public function testMailResult(result:*,event:TestEmailEvent):void{
			this.isUpdating=false;
			dispatcher(new ApplicationMessage(SystemProperties.NAME,SystemProperties.FORM_TEST_MAIL_RESULT));
		}
		
		[CommandError]
		public function testMailError(fault:*,event:TestEmailEvent):void{
			this.isUpdating=false;
		}
	}
}
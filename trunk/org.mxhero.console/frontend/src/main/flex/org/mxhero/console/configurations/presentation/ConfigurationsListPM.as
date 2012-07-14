package org.mxhero.console.configurations.presentation
{
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	
	import org.mxhero.console.configurations.application.ConfigurationsDestinations;
	import org.mxhero.console.configurations.application.event.EditConfigurationEvent;
	import org.mxhero.console.configurations.application.event.LoadConfigurationEvent;
	import org.mxhero.console.configurations.application.event.SaveLicenseEvent;
	import org.mxhero.console.configurations.application.event.ValidateLicenseEvent;
	import org.mxhero.console.configurations.presentation.license.License;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.infrastructure.AuthorizeHelper;

	[Landmark(name="main.dashboard.configurations.list")]
	public class ConfigurationsListPM
	{
		
		[Inject(id="configurationsCategories")]
		[Bindable]
		public var dataSource:Object;
		
		[Inject]
		[Bindable]
		public var configurationsViewPM:ConfigurationsViewPM;
		
		[Inject]
		[Bindable]
		public var contex:ApplicationContext;
		
		[Inject]
		[Bindable]
		public var authorizeHelper:AuthorizeHelper;
		
		[Bindable]
		public var authorizedDataSource:Object;
		
		[Bindable]
		public var isValid:Boolean;
		
		[Enter(time="every")]
		public function every():void{
			authorizedDataSource=authorizeHelper.authorizeList(dataSource);
			validateLicense();
		}
		
		public function childClickHandler(child:Object,category:Object):void{
			if(child.navigateTo == ConfigurationsDestinations.LICENSE){
				var window:License = new License();
				window.model=this;
				PopUpManager.addPopUp(window,ConfigurationsViewPM.container,true);
				PopUpManager.bringToFront(window);
				PopUpManager.centerPopUp(window);
			}else if(child.navigateTo == ConfigurationsDestinations.DOCUMENTATION){
				navigateToURL(new URLRequest(contex.configuration.documentationUrl),"Documentation");
			}else{
				configurationsViewPM.navigateTo(child.navigateTo);
			}
		}
		
		public function saveConfig():void{
			configurationsViewPM.dispatcher(new SaveLicenseEvent());
		}
		
		public function validateLicense():void{
			configurationsViewPM.dispatcher(new ValidateLicenseEvent(contex.configuration.license));
		}
		
		[CommandResult]
		public function saveLicenseResult(result:*,event:SaveLicenseEvent):void{
			configurationsViewPM.dispatcher(new LoadConfigurationEvent());
			configurationsViewPM.dispatcher(new ValidateLicenseEvent(contex.configuration.license));
		}
		
		[CommandResult]
		public function validateLicenseResult(result:*,event:ValidateLicenseEvent):void{
			isValid=result as Boolean;
		}
		
		[CommandError]
		public function validateLicenseError(faultEvent:FaultEvent, event:ValidateLicenseEvent) : void {
			isValid=false;
		}
	}
}
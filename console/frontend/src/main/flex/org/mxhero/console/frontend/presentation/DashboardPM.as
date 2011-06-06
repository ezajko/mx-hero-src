package org.mxhero.console.frontend.presentation
{
	import com.adobe.cairngorm.navigation.NavigationEvent;
	
	import mx.controls.Alert;
	import mx.core.Container;
	import mx.effects.SoundEffect;
	import mx.events.CloseEvent;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	
	import org.mxhero.console.configurations.application.ConfigurationsDestinations;
	import org.mxhero.console.features.application.FeaturesDestinations;
	import org.mxhero.console.frontend.application.MainDestination;
	import org.mxhero.console.frontend.application.event.LogoutEvent;
	import org.mxhero.console.frontend.application.message.ApplicationMessage;
	import org.mxhero.console.frontend.application.resources.DashboardProperties;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.Domain;
	import org.mxhero.console.frontend.infrastructure.AuthorizeHelper;
	import org.mxhero.console.reports.application.ReportsDestinations;
	import org.spicefactory.parsley.core.messaging.MessageProcessor;

	[Landmark(name="main.dashboard")]
	public class DashboardPM
	{
		[Bindable]
		private var rm:IResourceManager = ResourceManager.getInstance();

		[Bindable]
		public static var container:Container;
		
		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		
		[MessageDispatcher]
		public var dispatcher:Function;

		[Inject]
		[Bindable]			
		public var authorizeHelper:AuthorizeHelper;
		
		[Bindable]
		[CommandStatus(type="org.mxhero.console.frontend.application.event.LogoutEvent")]
		public var isLogingout:Boolean = false;
		
		[Enter(time="first")]
		public function enter():void
		{
			container.createComponentsFromDescriptors();
			dispatcher(NavigationEvent.createNavigateToEvent(MainDestination.HOME));
		}
		
		public function logout():void{
			if(context.selectedDomain!=null 
				&& authorizeHelper.checkUserForAuthority('ROLE_ADMIN')){
				Alert.show(rm.getString(DashboardProperties.NAME,DashboardProperties.ALERT_LOGOUT_DOMAIN_CONFIRM_TEXT),context.selectedDomain.domain,Alert.YES|Alert.NO,null,leaveDomainHandler);
			}else{
				Alert.show(rm.getString(DashboardProperties.NAME,DashboardProperties.ALERT_LOGOUT_CONFIRM_TEXT),"",Alert.YES|Alert.NO,null,logoutHandler);
			}
		}
		
		public function logoutHandler(event:CloseEvent):void{
			if(event.detail==Alert.YES){
				dispatcher(new LogoutEvent());
			}
		}

		public function leaveDomainHandler(event:CloseEvent):void{
			if(event.detail==Alert.YES){
				var message:String=rm.getString(DashboardProperties.NAME,DashboardProperties.DOMAIN_LEAVE_MESSAGE)+"("+context.selectedDomain.domain+")";
				dispatcher(new ApplicationMessage(null,null,message));
				context.selectedDomain=null;
				dispatcher(NavigationEvent.createNavigateToEvent(ConfigurationsDestinations.LIST));
				dispatcher(NavigationEvent.createNavigateToEvent(ReportsDestinations.LIST));
				dispatcher(NavigationEvent.createNavigateToEvent(FeaturesDestinations.LIST));
				dispatcher(NavigationEvent.createNavigateToEvent(MainDestination.HOME));
			}
		}
		
		public function hasToShowDomain(domain:Domain):Boolean{
			if(domain!=null && authorizeHelper.checkUserForAuthority('ROLE_ADMIN')){
				return true;
			}
			return false;
		}
		
	}

}
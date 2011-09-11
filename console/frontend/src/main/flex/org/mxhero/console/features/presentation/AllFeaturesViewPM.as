package org.mxhero.console.features.presentation
{
	import com.adobe.cairngorm.navigation.NavigationEvent;
	import com.adobe.cairngorm.navigation.state.ISelectedName;
	
	import mx.collections.ArrayCollection;
	import mx.core.Container;
	
	import org.mxhero.console.commons.infrastructure.Roles;
	import org.mxhero.console.features.application.event.GetFeaturesByDomainIdEvent;
	import org.mxhero.console.features.application.event.GetFeaturesEvent;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.infrastructure.AuthorizeHelper;

	[Landmark(name="main.dashboard.features")]
	public class AllFeaturesViewPM
	{
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function navigateTo(destination:String):void
		{
			dispatcher(NavigationEvent.createNavigateToEvent(destination));
		}
	
	}
}
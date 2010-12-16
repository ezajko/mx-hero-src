package org.mxhero.console.features.presentation
{
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	
	import org.mxhero.console.features.application.FeaturesDestinations;
	import org.mxhero.console.features.application.event.GetAccountsEvent;
	import org.mxhero.console.features.application.event.GetDomainAccountsEvent;
	import org.mxhero.console.features.application.event.GetFeaturesByDomainIdEvent;
	import org.mxhero.console.features.application.event.GetFeaturesEvent;
	import org.mxhero.console.features.presentation.feature.FeatureViewPM;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.Category;
	import org.mxhero.console.frontend.domain.EmailAccount;
	import org.mxhero.console.frontend.domain.Feature;

	[Landmark(name="main.dashboard.features.list")]
	public class AllFeaturesListPM
	{
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Bindable]
		public var isUpdating:Boolean = false;
		
		[Bindable]
		public var categories:ArrayCollection;
		
		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		
		[Inject]
		[Bindable]
		public var featureView:FeatureViewPM;
		
		[Inject]
		[Bindable]
		public var allFeaturesView:AllFeaturesViewPM;
		
		[Enter(time="every")]
		public function every():void{
			if(context.selectedDomain!=null){
				dispatcher(new GetFeaturesByDomainIdEvent(context.selectedDomain.id));
			}else {
				dispatcher(new GetFeaturesEvent());
			}
			isUpdating=true;
		}
		
		[CommandResult]
		public function findFeaturesByDomainIdResult(result:*,event:GetFeaturesByDomainIdEvent):void{
			categories=result;
			isUpdating=false;
		}
		
		[CommandError]
		public function findFeaturesByDomainIdError(fault:*,event:GetFeaturesByDomainIdEvent):void{
			isUpdating=false;
		}
		
		[CommandResult]
		public function findFeaturesResult(result:*,event:GetFeaturesEvent):void{
			categories=result;
			isUpdating=false;
		}
		
		[CommandError]
		public function findFeaturesError(fault:*,event:GetFeaturesEvent):void{
			isUpdating=false;
		}
		
		public function childClickHandler(child:Object,category:Object):void{
			featureView.selectedFeature=child as Feature;
			featureView.selectedCategory=category as Category;
			allFeaturesView.navigateTo(FeaturesDestinations.FEATURE_VIEW);
		}
		
	}
}
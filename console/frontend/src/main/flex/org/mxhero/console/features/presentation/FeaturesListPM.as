package org.mxhero.console.features.presentation
{
	import mx.collections.ArrayCollection;
	
	import org.mxhero.console.features.application.event.GetFeaturesByDomainIdEvent;
	import org.mxhero.console.features.application.event.GetFeaturesEvent;
	import org.mxhero.console.frontend.domain.ApplicationContext;

	[Landmark(name="main.dashboard.features.list")]
	public class FeaturesListPM
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
	}
}
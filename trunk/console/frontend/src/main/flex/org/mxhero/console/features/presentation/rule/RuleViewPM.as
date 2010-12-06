package org.mxhero.console.features.presentation.rule
{
	import org.mxhero.console.features.application.FeaturesDestinations;
	import org.mxhero.console.features.application.event.CreateDomainRuleEvent;
	import org.mxhero.console.features.application.event.CreateNoDomainRuleEvent;
	import org.mxhero.console.features.presentation.AllFeaturesViewPM;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.Category;
	import org.mxhero.console.frontend.domain.Feature;
	import org.mxhero.console.frontend.domain.FeatureRule;

	[Landmark(name="main.dashboard.features.rule")]
	public class RuleViewPM
	{

		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Bindable]
		public var rule:FeatureRule;
		
		[Bindable]
		public var feature:Feature;
		
		[Bindable]
		public var category:Category;
		
		[Inject]
		[Bindable]
		public var parent:AllFeaturesViewPM;
		
		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		
		[Bindable]
		public var isUpdating:Boolean = false;
		
		public function cancel():void{
			parent.navigateTo(FeaturesDestinations.FEATURE_VIEW);
		}
		
		
		public function save():void{
			//new rule
			if(isNaN(rule.id) || rule.id<0){
				if(context.selectedDomain!=null){
					dispatcher(new CreateDomainRuleEvent(rule,this.feature.id,context.selectedDomain.id));
				} else {
					dispatcher(new CreateNoDomainRuleEvent(rule,this.feature.id));
				}
			} 
			//edit rule
			else {
				
			}
			isUpdating=true;
		}
		
		[CommandResult]
		public function createDomainRuleResult(result:*,event:CreateDomainRuleEvent):void{
			parent.navigateTo(FeaturesDestinations.FEATURE_VIEW);
			isUpdating=false;
		}
		
		[CommandError]
		public function createDomainRuleError(fault:*,event:CreateDomainRuleEvent):void{
			isUpdating=false;
		}
		
		[CommandResult]
		public function createNoDomainRuleResult(result:*,event:CreateNoDomainRuleEvent):void{
			parent.navigateTo(FeaturesDestinations.FEATURE_VIEW);
			isUpdating=false;
		}
		
		[CommandError]
		public function createNoDomainRuleError(fault:*,event:CreateNoDomainRuleEvent):void{
			isUpdating=false;
		}
	}
}
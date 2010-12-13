package org.mxhero.console.features.presentation.rule
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	
	import org.mxhero.console.features.application.FeaturesDestinations;
	import org.mxhero.console.features.application.event.CreateDomainRuleEvent;
	import org.mxhero.console.features.application.event.CreateNoDomainRuleEvent;
	import org.mxhero.console.features.application.event.EditRuleEvent;
	import org.mxhero.console.features.application.event.GetDomainGroupsEvent;
	import org.mxhero.console.features.application.event.GetDomainsEvent;
	import org.mxhero.console.features.presentation.AllFeaturesViewPM;
	import org.mxhero.console.features.presentation.FromTo.FromTo;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.Category;
	import org.mxhero.console.frontend.domain.Feature;
	import org.mxhero.console.frontend.domain.FeatureRule;

	[Landmark(name="main.dashboard.features.rule")]
	public class RuleViewPM
	{
		
		private static var _refreshFunction:Function;

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
		
		[Enter(time="every")]
		public function every():void{
			if(context.selectedDomain==null){
				dispatcher(new GetDomainsEvent());
			} else {
				var newDomains:ArrayCollection = new ArrayCollection();
				newDomains.addItem(context.selectedDomain);
				context.domains=newDomains;
				dispatcher(new GetDomainGroupsEvent(context.selectedDomain.id));
			}
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
				dispatcher(new EditRuleEvent(rule));
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
		
		[CommandResult]
		public function editRuleResult(result:*,event:EditRuleEvent):void{
			parent.navigateTo(FeaturesDestinations.FEATURE_VIEW);
			isUpdating=false;
		}
		
		[CommandError]
		public function editRuleError(fault:*,event:EditRuleEvent):void{
			isUpdating=false;
		}

		public function refresh():void{
			if(refreshFunction!=null){
				refreshFunction();
			}
		}

		public static function get refreshFunction():Function
		{
			return _refreshFunction;
		}

		public static function set refreshFunction(value:Function):void
		{
			_refreshFunction = value;
			refreshFunction();
		}

	}
}
package org.mxhero.console.features.presentation.rule
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	
	import org.mxhero.console.commons.FromTo.FromTo;
	import org.mxhero.console.features.application.FeaturesDestinations;
	import org.mxhero.console.features.application.event.CreateDomainRuleEvent;
	import org.mxhero.console.features.application.event.CreateNoDomainRuleEvent;
	import org.mxhero.console.features.application.event.EditRuleEvent;
	import org.mxhero.console.features.presentation.AllFeaturesViewPM;
	import org.mxhero.console.frontend.application.event.GetAccountsEvent;
	import org.mxhero.console.frontend.application.event.GetDomainGroupsEvent;
	import org.mxhero.console.frontend.application.event.GetDomainsEvent;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.Category;
	import org.mxhero.console.frontend.domain.Feature;
	import org.mxhero.console.frontend.domain.FeatureRule;

	[Landmark(name="main.dashboard.features.rule")]
	public class RuleViewPM
	{
		
		private static var _refreshFunction:Function;
		
		private static var _realoadExternal:Function;

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
			if(RuleViewPM.refreshFunction!=null){
				RuleViewPM.refreshFunction();
			}
			if(RuleViewPM.realoadExternal!=null){
				RuleViewPM.realoadExternal();
			}
			if(context.selectedDomain==null){
				dispatcher(new GetDomainsEvent());
				dispatcher(new GetAccountsEvent());
			} else {
				var newDomains:ArrayCollection = new ArrayCollection();
				newDomains.addItem(context.selectedDomain);
				context.domains=newDomains;
				dispatcher(new GetDomainGroupsEvent(context.selectedDomain.domain));
				dispatcher(new GetAccountsEvent(context.selectedDomain.domain));
			}
			
		}
		
		public function filterAccount(accountFilter:String):void{
			if(context.selectedDomain==null){
				dispatcher(new GetAccountsEvent(null,accountFilter));
			}else{
				dispatcher(new GetAccountsEvent(context.selectedDomain.domain,accountFilter));
			}
		}
		
		public function filterDomain(domainFilter:String):void{
			if(context.selectedDomain==null){
				dispatcher(new GetDomainsEvent(domainFilter));
			}
		}
		
		public function save():void{
			//new rule
			if(isNaN(rule.id) || rule.id<0){
				if(context.selectedDomain!=null){
					dispatcher(new CreateDomainRuleEvent(rule,this.feature.id,context.selectedDomain.domain));
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

		public function refresh(doExternal:Boolean=true):void{
			if(refreshFunction!=null){
				refreshFunction();
			}
			if(realoadExternal!=null && doExternal){
				realoadExternal();
			}
		}

		public static function get refreshFunction():Function
		{
			return _refreshFunction;
		}

		public static function set refreshFunction(value:Function):void
		{
			_refreshFunction = value;
		}

		public static function get realoadExternal():Function
		{
			return _realoadExternal;
		}

		public static function set realoadExternal(value:Function):void
		{
			_realoadExternal = value;
			realoadExternal();
		}

		
	}
}
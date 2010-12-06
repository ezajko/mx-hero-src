package org.mxhero.console.features.presentation.feature
{
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	
	import org.mxhero.console.features.application.FeaturesDestinations;
	import org.mxhero.console.features.application.event.GetFeaturesByDomainIdEvent;
	import org.mxhero.console.features.application.event.GetRulesByDomainIdEvent;
	import org.mxhero.console.features.application.event.GetRulesEvent;
	import org.mxhero.console.features.application.event.RemoveRuleEvent;
	import org.mxhero.console.features.application.event.ToggleRuleStatusEvent;
	import org.mxhero.console.features.application.resources.FeatureViewProperties;
	import org.mxhero.console.features.presentation.AllFeaturesViewPM;
	import org.mxhero.console.features.presentation.rule.RuleViewPM;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.Category;
	import org.mxhero.console.frontend.domain.Feature;
	import org.mxhero.console.frontend.domain.FeatureRule;

	[Landmark(name="main.dashboard.features.view")]
	public class FeatureViewPM
	{
		[Inject]
		[Bindable]
		public var allFeatureView:AllFeaturesViewPM;
		
		[Bindable]
		private var rm:IResourceManager = ResourceManager.getInstance();
		
		[Bindable]
		public var selectedFeature:Feature;
		
		[Bindable]
		public var selectedCategory:Category;
		
		[Bindable]
		public var selectedRule:Object;
		
		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		
		[Inject]
		[Bindable]
		public var ruleModel:RuleViewPM;
		
		[Bindable]
		public var isUpdating:Boolean = false;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		public function goBack():void{
			allFeatureView.navigateTo(FeaturesDestinations.LIST);
		}
		
		public function removeRule():void{
			Alert.show(rm.getString(FeatureViewProperties.NAME,FeatureViewProperties.REMOVE_RULE_CONFIRMATION_TEXT),selectedRule.name,Alert.YES|Alert.NO,null,removeHandler);
		}
		
		public function removeHandler(event:CloseEvent):void{
			if(event.detail==Alert.YES){
				dispatcher(new RemoveRuleEvent(selectedRule.id));
			}
		}
		
		[CommandResult]
		public function removeResult (result:*, event:RemoveRuleEvent) : void {
			var itemPosition:Number= selectedFeature.rules.getItemIndex(selectedRule);
			if(itemPosition>-1){
				selectedFeature.rules.removeItemAt(itemPosition);
			}
		}
		
		public function changeRuleStatus():void{
			dispatcher(new ToggleRuleStatusEvent(selectedRule.id));
			isUpdating=true;
		}
		
		[CommandComplete]
		public function handleToggle(event:ToggleRuleStatusEvent) : void {
			isUpdating=false;
			loadFeatures();
		}
		
		[Enter(time="every")]
		public function every():void{
			loadFeatures();
		}
		
		private function loadFeatures():void{
			if(context.selectedDomain!=null){
				dispatcher(new GetRulesByDomainIdEvent(selectedFeature.id, context.selectedDomain.id));
			}else {
				dispatcher(new GetRulesEvent(selectedFeature.id));
			}
			isUpdating=true;
		}
		
		[CommandResult]
		public function findRulesByDomainIdResult(result:*,event:GetRulesByDomainIdEvent):void{
			if(result is FeatureRule){
				selectedFeature.rules=new ArrayCollection();
				selectedFeature.rules.addItem(result as FeatureRule);
			} else {
				selectedFeature.rules=result;
			}
			isUpdating=false;
		}
		
		[CommandError]
		public function findRulesByDomainIdError(fault:*,event:GetRulesByDomainIdEvent):void{
			isUpdating=false;
		}
		
		[CommandResult]
		public function findRulesResult(result:*,event:GetRulesEvent):void{
			if(result is FeatureRule){
				selectedFeature.rules=new ArrayCollection();
				selectedFeature.rules.addItem(result as FeatureRule);
			} else {
				selectedFeature.rules=result;
			}
			isUpdating=false;
		}
		
		[CommandError]
		public function findRulesError(fault:*,event:GetRulesEvent):void{
			isUpdating=false;
		}
		
		
		
		public function editRule():void{
			ruleModel.rule = (this.selectedRule as FeatureRule).clone();
			ruleModel.feature = this.selectedFeature;
			ruleModel.category = this.selectedCategory;
			this.allFeatureView.navigateTo(FeaturesDestinations.RULE);
		}
		
		public function newRule():void{
			ruleModel.rule = new FeatureRule();
			ruleModel.feature = this.selectedFeature;
			ruleModel.category = this.selectedCategory;
			this.allFeatureView.navigateTo(FeaturesDestinations.RULE);
		}
		
	}
}
package org.mxhero.console.features.presentation
{
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.events.ModuleEvent;
	import mx.modules.IModuleInfo;
	import mx.modules.ModuleManager;
	import mx.resources.ResourceManager;
	
	import org.mxhero.console.features.application.FeaturesDestinations;
	import org.mxhero.console.features.application.event.GetFeaturesByDomainIdEvent;
	import org.mxhero.console.features.application.event.GetFeaturesEvent;
	import org.mxhero.console.features.presentation.feature.FeatureViewPM;
	import org.mxhero.console.frontend.application.event.GetAccountsEvent;
	import org.mxhero.console.frontend.application.event.GetDomainAccountsEvent;
	import org.mxhero.console.frontend.application.message.LanguageChangedMessage;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.Category;
	import org.mxhero.console.frontend.domain.EmailAccount;
	import org.mxhero.console.frontend.domain.Feature;

	[Landmark(name="main.dashboard.features.list")]
	public class AllFeaturesListPM
	{
		
		public static const LOADED:String = "loaded";
		
		public static const PRELOADING:String = "preloading";
		
		[Bindable]
		public var loading:String;
		
		private var modulesToPreload:Array = new Array();
		
		[Bindable]
		public var state:String = "loaded";
		
		private var loadedFeatures:ArrayCollection = new ArrayCollection();
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Bindable]
		public var isUpdating:Boolean = false;
		
		[Bindable]
		public var categories:ArrayCollection;
		
		private var categoriesAux:ArrayCollection;
		
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
			categoriesAux=categories;
			this.categories=null;
			this.categories=categoriesAux;
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
			preload(categories);
		}
		
		[CommandError]
		public function findFeaturesByDomainIdError(fault:*,event:GetFeaturesByDomainIdEvent):void{
			isUpdating=false;
		}
		
		[CommandResult]
		public function findFeaturesResult(result:*,event:GetFeaturesEvent):void{
			categoriesAux=result;
			isUpdating=false;
			preload(categoriesAux);
		}
		
		[CommandError]
		public function findFeaturesError(fault:*,event:GetFeaturesEvent):void{
			isUpdating=false;
		}
		
		[MessageHandler]
		public function handleLanguageChange(message:LanguageChangedMessage):void{
			this.categories=null;
			this.categories=categoriesAux;
		}
		
		public function childClickHandler(child:Object,category:Object):void{
			featureView.selectedFeature=child as Feature;
			featureView.selectedCategory=category as Category;
			allFeaturesView.navigateTo(FeaturesDestinations.FEATURE_VIEW);
		}

		private function preload(categories:ArrayCollection):void{
			for each (var category:Object in categories){
				for each(var feature:Object in category.childs){
					if(!loadedFeatures.contains(feature.moduleUrl)){
						modulesToPreload.push(feature.moduleUrl);
					}
				}
			}
			if(modulesToPreload.length>0){
				this.state=PRELOADING;
				preloadAll();
			}else{
				this.state=LOADED;
				categories=categoriesAux;
			}
		}
		
		private function preloadAll(e:ModuleEvent=null):void{
			if(modulesToPreload!=null && modulesToPreload.length>0){
				var moduleUrl:String = modulesToPreload.pop() as String;
				var module:IModuleInfo = ModuleManager.getModule(moduleUrl);
				if(module!=null){
					module.addEventListener(ModuleEvent.READY,preloadAll);
					loading=module.url;
					module.load();
				}
			}else{
				this.state=LOADED;
				loading="";
				categories=categoriesAux;
			}
			if(e!=null && e.module.loaded && !loadedFeatures.contains(e.module.url)){
				loadedFeatures.addItem(e.module.url);
				ResourceManager.getInstance().update();
				//e.module.unload();
			}
		}
		
	}
}
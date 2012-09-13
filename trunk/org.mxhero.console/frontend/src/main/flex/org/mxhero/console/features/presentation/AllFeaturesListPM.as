/*
* mxHero is a platform that intends to provide a single point of development 
* and single point of distribution for email solutions and enhancements. It does this
* by providing an extensible framework for rapid development and deployment of
* email solutions.
* 
* Copyright (C) 2012  mxHero Inc.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.mxhero.console.features.presentation
{
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.events.ModuleEvent;
	import mx.modules.IModuleInfo;
	import mx.modules.Module;
	import mx.modules.ModuleManager;
	import mx.resources.ResourceManager;
	
	import org.mxhero.console.features.application.FeaturesDestinations;
	import org.mxhero.console.features.application.event.GetFeaturesByDomainIdEvent;
	import org.mxhero.console.features.application.event.GetFeaturesEvent;
	import org.mxhero.console.features.presentation.feature.FeatureViewPM;
	import org.mxhero.console.frontend.application.event.GetAccountsEvent;
	import org.mxhero.console.frontend.application.message.ApplicationErrorMessage;
	import org.mxhero.console.frontend.application.message.LanguageChangedMessage;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.Category;
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
		
		
		private var module:IModuleInfo;
		
		[Enter(time="every")]
		public function every():void{
			if(context.selectedDomain!=null){
				dispatcher(new GetFeaturesByDomainIdEvent(context.selectedDomain.domain));
			}else {
				dispatcher(new GetFeaturesEvent());
			}
			isUpdating=true;
		}
		
		[CommandResult]
		public function findFeaturesByDomainIdResult(result:*,event:GetFeaturesByDomainIdEvent):void{
			categoriesAux=result;
			isUpdating=false;
			preload(categoriesAux);
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
			if(child.enabled){
				featureView.selectedFeature=child as Feature;
				featureView.selectedCategory=category as Category;
				allFeaturesView.navigateTo(FeaturesDestinations.FEATURE_VIEW);
			}
		}

		private function preload(categories:ArrayCollection):void{
			for each (var category:Object in categories){
				for each(var feature:Object in category.childs){
					if(!loadedFeatures.contains(feature.moduleUrl)){
						modulesToPreload.push(feature.moduleUrl);
					}
				}
			}
			this.state=PRELOADING;
			loadNext();
		}
		
		private function loadNext(e:ModuleEvent=null):void{
		
			if(e!=null){
				ResourceManager.getInstance().update();
				e.module.removeEventListener(ModuleEvent.READY,loadNext);
				e.module.removeEventListener(ModuleEvent.ERROR,loadNext);
				e.module.addEventListener(ModuleEvent.PROGRESS,checkLoading);
				if(!loadedFeatures.contains(e.module.url)){
					loadedFeatures.addItem(e.module.url);
				}
			}
			if(modulesToPreload!=null && modulesToPreload.length>0){
				module= ModuleManager.getModule(modulesToPreload.pop() as String);
				loading=module.url+" 0%";
				module.addEventListener(ModuleEvent.READY,loadNext);
				module.addEventListener(ModuleEvent.ERROR,loadNext);
				module.addEventListener(ModuleEvent.PROGRESS,checkLoading);
				module.load();
			}else {
				this.state=LOADED;
				categories=categoriesAux;
			}
		}
		
		private function checkLoading(e:ModuleEvent):void{
			loading=e.module.url+" "+int(e.bytesLoaded/e.bytesTotal*100)+"%";
		}
		
	}
}
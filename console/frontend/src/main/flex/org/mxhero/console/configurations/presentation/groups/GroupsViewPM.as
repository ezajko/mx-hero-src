package org.mxhero.console.configurations.presentation.groups
{
	import flash.display.DisplayObject;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	
	import org.mxhero.console.configurations.application.ConfigurationsDestinations;
	import org.mxhero.console.configurations.application.event.LoadAllGroupsEvent;
	import org.mxhero.console.configurations.application.event.RemoveGroupEvent;
	import org.mxhero.console.configurations.application.resources.GroupsProperties;
	import org.mxhero.console.configurations.presentation.ConfigurationsViewPM;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.Group;

	[Landmark(name="main.dashboard.configurations.groups")]
	public class GroupsViewPM
	{
		[Bindable]
		public var isLoading:Boolean=false;

		[Bindable]
		public var selectGroup:Object;
		
		[Bindable]
		private var rm:IResourceManager = ResourceManager.getInstance();
		
		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		
		[Bindable]
		public var groups:ArrayCollection;
		
		[Inject]
		[Bindable]
		public var parentModel:ConfigurationsViewPM;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		private var _nameFilter:String=null;
		private var _descriptionFilter:String=null;
		
		public function goBack():void{
			parentModel.navigateTo(ConfigurationsDestinations.LIST);
		}
		
		[Enter(time="every")]
		public function every():void{
			loadGroups();
		}
		
		public function loadGroups():void{
			dispatcher(new LoadAllGroupsEvent(context.selectedDomain.id));
			isLoading=true;
		}
		
		[CommandResult]
		public function loadingResult (result:*, event:LoadAllGroupsEvent) : void {
			if (result is Group){
				groups=new ArrayCollection();
				groups.addItem(result);
			} else {
				groups=result;	
			}
			if(groups!=null){
				var sortByName:Sort=new Sort();
				sortByName.fields=[new SortField("name")];
				groups.sort=sortByName;
				groups.refresh()
			}
			isLoading=false;
		}
		
		[CommandError]
		public function loadingError (fault:*, event:LoadAllGroupsEvent) : void {
			isLoading=false;
		}
		
		public function newGroup(parent:DisplayObject):void{
			
		}
		
		public function editGroup():void{
			
		}
		
		public function removeGroup():void{
			Alert.show(rm.getString(GroupsProperties.NAME,GroupsProperties.REMOVE_GROUP_CONFIRMATION_TEXT),selectGroup.name,Alert.YES|Alert.NO,null,removeHandler);
		}
		
		public function removeHandler(event:CloseEvent):void{
			if(event.detail==Alert.YES){
				dispatcher(new RemoveGroupEvent(selectGroup.id));
			}
		}
		
		[CommandResult]
		public function removeResult (result:*, event:RemoveGroupEvent) : void {
			var itemPosition:Number= groups.getItemIndex(selectGroup);
			if(itemPosition>-1){
				groups.removeItemAt(itemPosition);
			}
		}
		
		public function filterGroups(name:String,description:String):void{
			_nameFilter=trim(name).toLowerCase();
			_descriptionFilter=trim(description).toLowerCase();
			groups.filterFunction=filterFuntion;
			groups.refresh()
		}
		
		public function filterFuntion(object:Object):Boolean{
			if(_nameFilter!=null && _nameFilter.length>0){
				if(object.name.toLowerCase().search(_nameFilter)==-1){
					return false;
				}
			}
			if(_descriptionFilter!=null && _descriptionFilter.length>0){
				if(object.description.toLowerCase().search(_descriptionFilter)==-1){
					return false;
				}
			}
			return true;
		}
		
		private function trim(s:String):String { 
			return s ? s.replace(/^\s+|\s+$/gs, '') : ""; 
		}
	}
}
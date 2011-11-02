package org.mxhero.console.configurations.presentation.groups
{
	import flash.display.DisplayObject;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;
	import mx.rpc.events.FaultEvent;
	
	import org.mxhero.console.commons.infrastructure.ErrorTranslator;
	import org.mxhero.console.configurations.application.ConfigurationsDestinations;
	import org.mxhero.console.configurations.application.command.LoadEmailAccountsWithNoGroupCommand;
	import org.mxhero.console.configurations.application.event.EditGroupEvent;
	import org.mxhero.console.configurations.application.event.InsertGroupEvent;
	import org.mxhero.console.configurations.application.event.LoadAllGroupsEvent;
	import org.mxhero.console.configurations.application.event.LoadEmailAccountsByGroupEvent;
	import org.mxhero.console.configurations.application.event.LoadEmailAccountsWithNoGroupEvent;
	import org.mxhero.console.configurations.application.event.RemoveGroupEvent;
	import org.mxhero.console.configurations.application.resources.GroupsProperties;
	import org.mxhero.console.configurations.presentation.ConfigurationsViewPM;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.EmailAccount;
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
		
		[Inject]
		[Bindable]
		public var parentModel:ConfigurationsViewPM;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		private var _nameFilter:String=null;
		private var _descriptionFilter:String=null;
		
		[Bindable]
		public var groupAccounts:ArrayCollection;
		[Bindable]
		public var noGroupAccounts:ArrayCollection;
		
		private var groupShow:GroupShow;
		
		public function goBack():void{
			parentModel.navigateTo(ConfigurationsDestinations.LIST);
		}
		
		[Enter(time="every")]
		public function every():void{
			loadGroups();
		}
		
		public function loadGroups():void{
			dispatcher(new LoadAllGroupsEvent(context.selectedDomain.domain));
			isLoading=true;
		}
		
		[CommandComplete]
		public function loadingComplete (event:LoadAllGroupsEvent) : void {
			isLoading=false;
		}

		public function newGroup(parent:DisplayObject):void{
			groupShow=new GroupShow();
			groupShow.model=this;
			groupShow.currentState="new";
			groupShow.group=new Group();
			groupShow.group.domain=context.selectedDomain.domain;
			PopUpManager.addPopUp(groupShow,parent,true);
			PopUpManager.centerPopUp(groupShow);
			PopUpManager.bringToFront(groupShow);
			this.groupAccounts=new ArrayCollection();
			dispatcher(new LoadEmailAccountsWithNoGroupEvent(context.selectedDomain.domain));
			isLoading=true;
		}

		[CommandResult]
		public function loadNoGroupResult (result:*, event:LoadEmailAccountsWithNoGroupEvent) : void {
			isLoading=false;
			if(result is EmailAccount){
				this.noGroupAccounts=new ArrayCollection();
				noGroupAccounts.addItem(result);
			} else {
				this.noGroupAccounts=result;
			}
		}
		
		[CommandError]
		public function loadNoGroupError (faultEvent:FaultEvent, event:LoadEmailAccountsWithNoGroupEvent) : void {
			isLoading=false;
			PopUpManager.removePopUp(groupShow);
		}
		
		public function editGroup(parent:DisplayObject):void{
			groupShow=new GroupShow();
			groupShow.model=this;
			groupShow.currentState="edit";
			groupShow.group=(selectGroup as Group).clone();
			PopUpManager.addPopUp(groupShow,parent,true);
			PopUpManager.centerPopUp(groupShow);
			PopUpManager.bringToFront(groupShow);
			dispatcher(new LoadEmailAccountsByGroupEvent(this.selectGroup.name,this.selectGroup.domain));
			isLoading=true;
		}

		[CommandResult]
		public function loadMembersResult (result:*, event:LoadEmailAccountsByGroupEvent) : void {

			if(result is EmailAccount){
				this.groupAccounts=new ArrayCollection();
				groupAccounts.addItem(result);
			} else if(result==null){
				this.groupAccounts=new ArrayCollection();
			} else {
				this.groupAccounts=result;
			}
			dispatcher(new LoadEmailAccountsWithNoGroupEvent(context.selectedDomain.domain));
		}
		
		[CommandError]
		public function loadMembersError (faultEvent:FaultEvent, event:LoadEmailAccountsByGroupEvent) : void {
			isLoading=false;
			PopUpManager.removePopUp(groupShow);
		}
		
		public function removeGroup():void{
			Alert.show(rm.getString(GroupsProperties.NAME,GroupsProperties.REMOVE_GROUP_CONFIRMATION_TEXT),selectGroup.name,Alert.YES|Alert.NO,null,removeHandler);
		}
		
		public function removeHandler(event:CloseEvent):void{
			if(event.detail==Alert.YES){
				dispatcher(new RemoveGroupEvent(selectGroup.name,selectGroup.domain));
			}
		}
		
		[CommandResult]
		public function removeResult (result:*, event:RemoveGroupEvent) : void {
			var itemPosition:Number= context.groups.getItemIndex(selectGroup);
			if(itemPosition>-1){
				context.groups.removeItemAt(itemPosition);
			}
		}
		
		public function insertGroup(newGroup:Group):void{
			dispatcher(new InsertGroupEvent(newGroup,groupAccounts));
			isLoading=true;
		}
		
		[CommandResult]
		public function insertResult (result:*, event:InsertGroupEvent) : void {
			isLoading=false;
			loadGroups();
			PopUpManager.removePopUp(groupShow);
		}
		
		[CommandError]
		public function insertError (faultEvent:FaultEvent, event:InsertGroupEvent) : void {
			isLoading=false;
			groupShow.errorText.showError(ErrorTranslator.translate(faultEvent.fault.faultCode));
		}
		
		public function updateGroup(group:Group):void{
			isLoading=true;
			dispatcher(new EditGroupEvent(group,this.groupAccounts));
		}

		[CommandResult]
		public function editResult (result:*, event:EditGroupEvent) : void {
			isLoading=false;
			loadGroups();
			PopUpManager.removePopUp(groupShow);
		}
		
		[CommandError]
		public function editError (faultEvent:FaultEvent, event:EditGroupEvent) : void {
			isLoading=false;
			groupShow.errorText.showError(ErrorTranslator.translate(faultEvent.fault.faultCode));
		}
		
		public function filterGroups(name:String,description:String):void{
			_nameFilter=trim(name).toLowerCase();
			_descriptionFilter=trim(description).toLowerCase();
			context.groups.filterFunction=filterFuntion;
			context.groups.refresh()
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
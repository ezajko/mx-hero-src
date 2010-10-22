package org.mxhero.console.configurations.presentation
{
	import org.mxhero.console.frontend.infrastructure.AuthorizeHelper;

	[Landmark(name="main.dashboard.configurations.list")]
	public class ConfigurationsListPM
	{
		
		[Inject(id="configurationsCategories")]
		[Bindable]
		public var dataSource:Object;
		
		[Inject]
		[Bindable]
		public var configurationsViewPM:ConfigurationsViewPM;
		
		[Inject]
		[Bindable]
		public var authorizeHelper:AuthorizeHelper;
		
		[Bindable]
		public var authorizedDataSource:Object;
		
		[Enter(time="every")]
		public function every():void{
			authorizedDataSource=authorizeHelper.authorizeList(dataSource);
		}
		
		public function childClickHandler(child:Object):void{
			configurationsViewPM.navigateTo(child.navigateTo);
		}
	}
}
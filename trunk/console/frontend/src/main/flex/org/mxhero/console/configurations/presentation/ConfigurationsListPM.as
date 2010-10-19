package org.mxhero.console.configurations.presentation
{
	[Landmark(name="main.dashboard.configurations.list")]
	public class ConfigurationsListPM
	{

		[Inject(id="configurationsCategories")]
		[Bindable]
		public var dataSource:Object;
		
		[Bindable]
		public var authorizedDataSource:Object;
		
		[Enter(time="every")]
		public function every():void{
			authorize();
		}
		
		public function authorize():void{
			authorizedDataSource=dataSource;
		}
		
		
	}
}
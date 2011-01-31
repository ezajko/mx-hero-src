package org.mxhero.console.reports.presentation.reports
{
	import org.mxhero.console.reports.application.ReportsDestinations;
	import org.mxhero.console.reports.presentation.ReportsViewPM;
	
	[Landmark(name="main.dashboard.reports.custom")]
	public class CustomPM
	{
		
		public static var refresh:Function;
		
		[Inject]
		[Bindable]
		public var parentModel:ReportsViewPM;
		
		[Enter(time="every")]
		public function every():void{
			if(refresh!=null){
				refresh();
			}
		}
		
		public function goBack():void{
			parentModel.navigateTo(ReportsDestinations.LIST);
			
		}
	}
}
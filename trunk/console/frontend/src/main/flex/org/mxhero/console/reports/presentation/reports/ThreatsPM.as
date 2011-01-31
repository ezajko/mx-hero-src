package org.mxhero.console.reports.presentation.reports
{
	import org.mxhero.console.reports.application.ReportsDestinations;
	import org.mxhero.console.reports.presentation.ReportsViewPM;

	[Landmark(name="main.dashboard.reports.threats")]
	public class ThreatsPM
	{
		[Inject]
		[Bindable]
		public var parentModel:ReportsViewPM;
		
		public function goBack():void{
			parentModel.navigateTo(ReportsDestinations.LIST);
		}
	}
}
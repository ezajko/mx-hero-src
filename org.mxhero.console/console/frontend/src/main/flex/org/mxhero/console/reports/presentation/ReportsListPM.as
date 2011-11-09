package org.mxhero.console.reports.presentation
{
	import org.mxhero.console.frontend.application.message.LanguageChangedMessage;
	import org.mxhero.console.frontend.infrastructure.AuthorizeHelper;

	[Landmark(name="main.dashboard.reports.list")]
	public class ReportsListPM
	{
			
		[Inject(id="reportsCategories")]
		[Bindable]
		public var dataSource:Object;
		
		[Inject]
		[Bindable]
		public var reportsViewPM:ReportsViewPM;
		
		[Inject]
		[Bindable]
		public var authorizeHelper:AuthorizeHelper;
		
		[Bindable]
		public var authorizedDataSource:Object=null;
		
		[Enter(time="every")]
		public function every():void{
			authorizedDataSource=authorizeHelper.authorizeList(dataSource);
		}
		
		public function childClickHandler(child:Object,category:Object):void{
			reportsViewPM.navigateTo(child.navigateTo);
		}
		
		[MessageHandler]
		public function handleLanguageChange(message:LanguageChangedMessage):void{
			this.authorizedDataSource=null;
			this.authorizedDataSource=authorizeHelper.authorizeList(dataSource);
		}
	}
}
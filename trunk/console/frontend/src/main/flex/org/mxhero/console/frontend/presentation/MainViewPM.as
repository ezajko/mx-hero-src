package org.mxhero.console.frontend.presentation
{
	import com.adobe.cairngorm.navigation.NavigationEvent;

	public class MainViewPM
	{
		public function MainViewPM()
		{
		}
		
		[MessageDispatcher]
		public var dispatcher:Function
		
		private function navigateTo(destination:String):void
		{
			dispatcher(NavigationEvent.createNavigateToEvent(destination));    
		}
	}
}
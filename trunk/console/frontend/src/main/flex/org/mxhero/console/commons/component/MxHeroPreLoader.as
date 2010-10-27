package org.mxhero.console.commons.component
{
	import flash.events.ProgressEvent;
	
	import mx.preloaders.SparkDownloadProgressBar;

	public class MxHeroPreLoader extends SparkDownloadProgressBar
	{

		[Embed(source="/images/logo_preloader.png")]
		[Bindable]
		public var preloaderImage:Class;
		
		public function MxHeroPreLoader(){
			super();
		}
		
		// Override to set a background image.     
		override public function get backgroundImage():Object{
			return preloaderImage;
		}
		
		// Override to return true so progress bar appears
		// during initialization.       
		override protected function showDisplayForInit(elapsedTime:int, 
													   count:int):Boolean {
			return true;
		}
		
		// Override to return true so progress bar appears during download.     
		override protected function showDisplayForDownloading(
			elapsedTime:int, event:ProgressEvent):Boolean {
			return true;
		}		
		
	}
}
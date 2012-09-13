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
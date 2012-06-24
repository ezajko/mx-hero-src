package net.asciiworks.tlf.supportClasses {
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.geom.Point;
	import flash.geom.Rectangle;

    public class Icons {
		
		[Embed (source="nicEditorIcons.gif" )]
		private static var ICONS_DATA:Class;
		
		public static function getIcon(iconNum:int):BitmapData {
			var iconsBitmap:Bitmap = new ICONS_DATA();
			var bd:BitmapData = new BitmapData(18,18);
			var rect:Rectangle = new Rectangle(iconNum*18,0,18,18);
			var point:Point = new Point();
			
			bd.copyPixels(iconsBitmap.bitmapData,rect,point);
			
			return bd;
		}
    }
}
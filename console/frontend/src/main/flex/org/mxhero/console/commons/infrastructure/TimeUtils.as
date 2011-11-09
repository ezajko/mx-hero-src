package org.mxhero.console.commons.infrastructure
{
	public class TimeUtils
	{

		private static var systemDate:Date = new Date();
		
		public static function offsetToTZ(value:Number):String{
			return ((value<0)? "+" : "-")
					+((int)((value/60)%24<10) ? "0" : "")+ (int)((value/60)%24)
					+":"+((int)(value%60)<10 ? "0" : "")+ (int)(value%60); 
		}

		public static function sytemTZ():String{		
			return offsetToTZ(systemDate.getTimezoneOffset());
		}
	}
}
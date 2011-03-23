package org.mxhero.console.reports.application.resources
{
	public class ReportsImages
	{
		[Embed(source="/images/reports/export.png")] 
		[Bindable]
		public static var EXPORT:Class; 
		
		[Embed(source="/images/reports/refresh.png")] 
		[Bindable]
		public static var REFRESH:Class; 
		
		[Embed(source="/images/reports/zoom_out.png")] 
		[Bindable]
		public static var ZOOM_OUT:Class; 

		
		[Embed(source="/images/reports/queue/close.png")] 
		[Bindable]
		public static var CLOSE:Class; 
		
		[Embed(source="/images/reports/queue/open.png")] 
		[Bindable]
		public static var OPEN:Class; 
		
		[Embed(source="/images/reports/queue/rejected.png")] 
		[Bindable]
		public static var REJECTED:Class; 
		
		[Embed(source="/images/reports/queue/waiting.png")] 
		[Bindable]
		public static var WAITING:Class; 
		
		[Embed(source="/images/reports/queue/delivered.png")] 
		[Bindable]
		public static var DELIVERED:Class; 

		[Embed(source="/images/reports/fromto/dropped.png")] 
		[Bindable]
		public static var DROPPED_FROMTO:Class; 
		
		[Embed(source="/images/reports/fromto/delivered.png")] 
		[Bindable]
		public static var DELIVERED_FROMTO:Class; 
		
		[Embed(source="/images/reports/more.png")] 
		[Bindable]
		public static var MORE:Class; 
	}
}
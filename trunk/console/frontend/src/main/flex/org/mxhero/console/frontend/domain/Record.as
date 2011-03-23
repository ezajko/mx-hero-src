package org.mxhero.console.frontend.domain
{
	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.RecordVO")]
	public class Record
	{
		
		public var insertDate:Date;
		
		public var messageId:String;
		
		public var phase:String;
		
		public var sender:String;
		
		public var recipient:String;
		
		public var subject:String;
		
		public var from:String;
		
		public var toRecipients:String;
		
		public var ccRecipients:String;
		
		public var bccRecipients:String;
		
		public var ngRecipients:String;
		
		public var sentDate:Date;
		
		public var bytesSize:Number;
		
		public var state:String;
		
		public var stateReason:String;
		
		public var flow:String;
	}
}
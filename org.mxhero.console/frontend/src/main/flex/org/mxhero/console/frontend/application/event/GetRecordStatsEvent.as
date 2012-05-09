package org.mxhero.console.frontend.application.event
{
	public class GetRecordStatsEvent
	{
		public var sequence:Number;
		public var insertDate:Date;
		
		public function GetRecordStatsEvent(sequence:Number,insertDate:Date)
		{
			this.sequence = sequence;
			this.insertDate = insertDate;
		}
	}
}
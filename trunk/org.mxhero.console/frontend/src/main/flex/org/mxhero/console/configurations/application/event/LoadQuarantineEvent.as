package org.mxhero.console.configurations.application.event
{
	public class LoadQuarantineEvent
	{
		public var domain:String;
		
		public function LoadQuarantineEvent(domain:String)
		{
			this.domain=domain;
		}
	}
}
package org.mxhero.console.configurations.application.event
{
	import org.mxhero.console.frontend.domain.Quarantine;

	public class SaveQuarantineEvent
	{
		public var qurantine:Quarantine;
		
		public function SaveQuarantineEvent(qurantine:Quarantine)
		{
			this.qurantine=qurantine;
		}
	}
}
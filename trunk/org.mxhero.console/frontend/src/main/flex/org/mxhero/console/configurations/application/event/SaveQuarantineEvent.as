package org.mxhero.console.configurations.application.event
{
	import org.mxhero.console.frontend.domain.Quarantine;

	public class SaveQuarantineEvent
	{
		public var quarantine:Quarantine;
		
		public function SaveQuarantineEvent(quarantine:Quarantine)
		{
			this.quarantine=quarantine;
		}
	}
}
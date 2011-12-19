package org.mxhero.console.configurations.application.event
{
	import mx.collections.ArrayCollection;
	
	import org.mxhero.console.frontend.domain.Group;

	public class InsertGroupEvent
	{
		public var group:Group;

		public function InsertGroupEvent(group:Group)
		{
			this.group=group;
		}
	}
}
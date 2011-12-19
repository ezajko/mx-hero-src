package org.mxhero.console.configurations.application.event
{
	import mx.collections.ArrayCollection;
	
	import org.mxhero.console.frontend.domain.Group;

	public class EditGroupEvent
	{
		public var group:Group;
		
		public function EditGroupEvent(group:Group)
		{
			this.group=group;
		}
	}
}
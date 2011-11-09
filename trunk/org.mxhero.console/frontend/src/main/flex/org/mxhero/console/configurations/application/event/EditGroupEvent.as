package org.mxhero.console.configurations.application.event
{
	import mx.collections.ArrayCollection;
	
	import org.mxhero.console.frontend.domain.Group;

	public class EditGroupEvent
	{
		public var group:Group;
		
		public var members:ArrayCollection;
		
		public function EditGroupEvent(group:Group,members:ArrayCollection)
		{
			this.group=group;
			this.members=members;
		}
	}
}
package org.mxhero.console.configurations.application.event
{
	import mx.collections.ArrayCollection;
	
	import org.mxhero.console.frontend.domain.Group;

	public class InsertGroupEvent
	{
		public var group:Group;
		
		public var domainId:Number;
		
		public var members:ArrayCollection;
		
		public function InsertGroupEvent(group:Group, domainId:Number, members:ArrayCollection)
		{
			this.group=group;
			this.domainId=domainId;
			this.members=members;
		}
	}
}
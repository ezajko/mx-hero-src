package org.mxhero.console.configurations.application.event
{
	import org.mxhero.console.frontend.domain.ApplicationUser;

	public class EditApplicationUserEvent
	{
		public var user:ApplicationUser;
		
		public function EditApplicationUserEvent(user:ApplicationUser)
		{
			this.user=user;
		}
	}
}
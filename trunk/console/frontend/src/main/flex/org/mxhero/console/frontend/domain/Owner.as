package org.mxhero.console.frontend.domain
{
	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.OwnerVO")]
	public class Owner
	{
		public var id:Number
		public var email:String;
		
		public function clone():Owner{
			var clonedOwner:Owner = new Owner();
			clonedOwner.email=this.email;
			clonedOwner.id=this.id;
			return clonedOwner;
		}
	}
}
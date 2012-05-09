package org.mxhero.console.frontend.domain
{
	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.DomainAdLdapPropertyVO")]
	public class DomainAdLdapProperty
	{
		public var key:String;
		public var name:String;
		
		public function clone():DomainAdLdapProperty{
			var cloned:DomainAdLdapProperty = new DomainAdLdapProperty();
			cloned.key=this.key;
			cloned.name=this.name;
			return cloned;
		}
	}
}
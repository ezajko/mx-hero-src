package org.mxhero.console.frontend.domain
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.DomainVO")]
	public class Domain
	{
		
		public var domain:String;
		public var server:String;
		public var creationDate:Date;
		public var owner:Owner;
		public var updatedDate:Date;
		public var aliases:ArrayCollection;
		public var adLdap:DomainAdLdap;
		
		public function clone():Domain{
			var clonedDomain:Domain=new Domain();
			clonedDomain.creationDate=this.creationDate;
			clonedDomain.domain=this.domain;
			clonedDomain.server=this.server;
			clonedDomain.updatedDate=this.updatedDate;

			if(this.owner!=null){
				clonedDomain.owner=this.owner.clone();
			}
			if(this.aliases!=null){
				clonedDomain.aliases=new ArrayCollection();
				clonedDomain.aliases.addAll(this.aliases);
			}
			if(this.adLdap!=null){
				clonedDomain.adLdap=this.adLdap.clone();
			}
			return clonedDomain;
		}
	}
}
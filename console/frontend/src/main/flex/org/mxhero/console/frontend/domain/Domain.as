package org.mxhero.console.frontend.domain
{
	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.DomainVO")]
	public class Domain
	{
		public var id:Number;
		public var domain:String;
		public var server:String;
		public var creationDate:Date;
		public var owner:Owner;
		public var updatedDate:Date;
		
		public function clone():Domain{
			var clonedDomain:Domain=new Domain();
			clonedDomain.creationDate=this.creationDate;
			clonedDomain.domain=this.domain;
			clonedDomain.id=this.id;
			clonedDomain.server=this.server;
			clonedDomain.updatedDate=this.updatedDate;
			if(this.owner!=null){
				clonedDomain.owner=this.owner.clone();
			}
			return clonedDomain;
		}
	}
}
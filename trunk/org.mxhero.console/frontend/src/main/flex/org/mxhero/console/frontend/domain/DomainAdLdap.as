package org.mxhero.console.frontend.domain
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.DomainAdLdapVO")]
	public class DomainAdLdap
	{
		public var domainId:String;
		public var directoryType:String;
		public var addres:String;
		public var port:String;
		public var sslFlag:Boolean;
		public var user:String;
		public var password:String;
		public var filter:String;
		public var base:String;
		public var nextUpdate:Date;
		public var lastUpdate:Date;
		public var error:String;
		public var overrideFlag:Boolean;
		public var dnAuthenticate:String;
		public var accountProperties:ArrayCollection;
		
		public function clone():DomainAdLdap{
			var clonedAdLdap:DomainAdLdap = new DomainAdLdap();
			clonedAdLdap.domainId=this.domainId;
			clonedAdLdap.directoryType=this.directoryType;
			clonedAdLdap.addres=this.addres;
			clonedAdLdap.port=this.port;
			clonedAdLdap.sslFlag=this.sslFlag;
			clonedAdLdap.user=this.user;
			clonedAdLdap.password=this.password;
			clonedAdLdap.filter=this.filter;
			clonedAdLdap.base=this.base;
			clonedAdLdap.nextUpdate=this.nextUpdate;
			clonedAdLdap.lastUpdate=this.lastUpdate;
			clonedAdLdap.error=this.error;
			clonedAdLdap.overrideFlag=this.overrideFlag;
			clonedAdLdap.dnAuthenticate=this.dnAuthenticate;
			if(this.accountProperties!=null){
				clonedAdLdap.accountProperties=new ArrayCollection();
				clonedAdLdap.accountProperties.addAll(this.accountProperties);
			}
			return clonedAdLdap;
		}
	}
}
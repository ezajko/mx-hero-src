package org.mxhero.console.frontend.domain
{
	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.GroupVO")]
	public class Group
	{
		public var domain:String;
		public var name:String;
		public var description:String;
		public var createdDate:Date;
		public var updatedDate:Date;
		
		public function clone():Group{
			var clonedGroup:Group = new Group();
			clonedGroup.domain=this.domain;
			clonedGroup.name=this.name;
			clonedGroup.description=this.description;
			clonedGroup.name=this.name;
			clonedGroup.createdDate=this.createdDate;
			clonedGroup.updatedDate=this.updatedDate;
			return clonedGroup;
		}
	}
}
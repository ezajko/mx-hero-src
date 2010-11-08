package org.mxhero.console.frontend.domain
{
	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.GroupVO")]
	public class Group
	{
		public var id:Number;
		public var name:String;
		public var description:String;
		public var createdDate:Date;
		public var updatedDate:Date;
	}
}
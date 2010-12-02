package org.mxhero.console.frontend.domain
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.FeatureVO")]
	public class Feature
	{
		public var id:Number;
		private var _label:String;
		public var description:String;
		public var explain:String;
		public var rules:ArrayCollection;
		
		public function get label():String
		{
			if(rules!=null && rules.length>0){
				return _label+"("+rules.length+")";
			}
			return _label;
		}

		public function set label(value:String):void
		{
			_label = value;
		}

		public function get realLabel():String
		{
			return _label;
		}

		public function set realLabel(value:String):void
		{
			_label = value;
		}


	}
}
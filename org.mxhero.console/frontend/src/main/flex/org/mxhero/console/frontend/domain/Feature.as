/*
* mxHero is a platform that intends to provide a single point of development 
* and single point of distribution for email solutions and enhancements. It does this
* by providing an extensible framework for rapid development and deployment of
* email solutions.
* 
* Copyright (C) 2012  mxHero Inc.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.mxhero.console.frontend.domain
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceManager;

	[Bindable]
	[RemoteClass(alias="org.mxhero.console.backend.vo.FeatureVO")]
	public class Feature
	{
		private var rm:IResourceManager=ResourceManager.getInstance();
		
		public var id:Number;
		private var _label:String;
		private var _lastLabel:String;
		private var _description:String;
		private var _lastDescription:String;
		private var _explain:String;
		private var _lastExplain:String;
		public var rules:ArrayCollection;
		public var moduleUrl:String;
		public var moduleReportUrl:String;
		public var defaultAdminOrder:String;
		private var _component:String;
		public var enabled:Boolean;

		public function Feature(){
			rm.addEventListener("change",dispatchChange);
		}
		
		private function dispatchChange(event:Event):void
		{
			dispatchEvent(new Event("change"));
		}
		
		[Bindable("change")]
		public function get label():String
		{
			var labelResult:String=rm.getString("features_modules",this._label);
			if(labelResult!=null){
				_lastLabel=labelResult;
				if(rules!=null && rules.length>0){
					return labelResult+"("+rules.length+")";
				}else{
					return labelResult;
				}
			}else if(_lastLabel!=null){
				return _lastLabel;
			}
			return _label;
		}

		[Bindable("change")]
		public function get simpleLabel():String
		{
			var labelResult:String=rm.getString("features_modules",this._label);
			if(labelResult!=null){
				_lastLabel=labelResult;
				return labelResult;
			}else if(_lastLabel!=null){
				return _lastLabel;
			}
			return _label;
		}
		
		public function set label(value:String):void
		{
			_label = value;
		}

		[Bindable("change")]
		public function get description():String
		{
			var descriptionResult:String=rm.getString("features_modules",this._description);
			if(descriptionResult!=null){
				_lastDescription = descriptionResult;
				return descriptionResult;
			}else if(_lastDescription!=null){
				return _lastDescription;
			}
			return _description;
		}

		public function set description(value:String):void
		{
			_description = value;
		}

		[Bindable("change")]
		public function get explain():String
		{
			var explainResult:String=rm.getString("features_modules",this._explain);
			if(explainResult!=null){
				_lastExplain=explainResult;
				return explainResult;
			}else if(_lastExplain!=null){
				return _lastExplain;
			}
			return _explain;
		}

		public function set explain(value:String):void
		{
			_explain = value;
		}

		public function get component():String
		{
			if(_component!=null && _component.lastIndexOf(".")>-1){
				return _component.substring(_component.lastIndexOf(".")+1);
			}
			return _component;
		}

		public function set component(value:String):void
		{
			_component = value;
		}

	}
}
package org.mxhero.console.commons.feature
{
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;

	public interface IFeature extends IEventDispatcher
	{
		function isValid():Boolean;
		
		function setProperties(data:ArrayCollection):void;
		
		function getProperties():ArrayCollection;
		
		function getValidationError():String;
		
		function fixedTo():Boolean;
		
		function fixedFrom():Boolean;
		
		function fixedToValue():String;
		
		function fixedFromValue():String;
	}
}
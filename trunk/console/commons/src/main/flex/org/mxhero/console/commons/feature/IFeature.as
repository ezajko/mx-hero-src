package org.mxhero.console.commons.feature
{
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;

	public interface IFeature extends IEventDispatcher
	{
		function isValid():Boolean;
		
		function setData(data:ArrayCollection):void;
		
		function getData():ArrayCollection;
	}
}
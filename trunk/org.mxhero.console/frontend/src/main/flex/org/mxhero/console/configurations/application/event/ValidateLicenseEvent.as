package org.mxhero.console.configurations.application.event
{
	public class ValidateLicenseEvent
	{
		public var license:String;
		
		public function ValidateLicenseEvent(license:String)
		{
			this.license=license;
		}
	}
}
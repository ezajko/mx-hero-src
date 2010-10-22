package org.mxhero.console.configurations.presentation.domains
{
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	
	import org.mxhero.console.configurations.application.event.LoadAllDomainsEvent;
	import org.mxhero.console.frontend.domain.Domain;

	[Landmark(name="main.dashboard.configurations.domains")]
	public class DomainsViewPM
	{
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		[Bindable]
		public var domains:ArrayCollection;
		
		[Enter(time="every")]
		public function every():void{
			loadDomains();
		}
		
		public function loadDomains():void{
			dispatcher(new LoadAllDomainsEvent());
		}
		
		[CommandResult]
		public function loadingResult (result:*, event:LoadAllDomainsEvent) : void {
			if (result is Domain){
				domains=new ArrayCollection();
				domains.addItem(result);
			} else {
				domains=result;	
			}
			if(domains!=null){
				var sortByDomain:Sort=new Sort();
				sortByDomain.fields=[new SortField("domain")];
				domains.sort=sortByDomain;
			}
		}
	}
}
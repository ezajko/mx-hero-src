package org.mxhero.console.frontend.infrastructure
{
	import mx.collections.ArrayCollection;
	import mx.utils.ObjectUtil;
	
	import org.mxhero.console.commons.domain.LCCategory;
	import org.mxhero.console.frontend.domain.ApplicationContext;
	import org.mxhero.console.frontend.domain.Authority;

	public class AuthorizeHelper
	{

		[Inject]
		[Bindable]
		public var context:ApplicationContext;
		
		public function authorizeList(list:Object):Object{
			var authorizedList:ArrayCollection = new ArrayCollection();		
			
			for each (var category:Object in list){
				if(category.hasOwnProperty("requiredAuthority") &&
					category.requiredAuthority!=null){
					if(checkUserForAuthority(category.requiredAuthority)){
						authorizedList.addItem(getCategory(category));
					}
				}else{
					authorizedList.addItem(getCategory(category));
				}
			}
			return authorizedList;
		}
		
		public function checkUserForAuthority(authority:String):Boolean{
			if (context==null || 
				context.applicationUser==null){
				return false;
			}
			for each(var userAuthority:Authority in context.applicationUser.authorities){
				if(authority==userAuthority.authority){
					return true;
				}
			}
			return false;
		}
		
		private function getCategory(category:Object):Object{
			var newChilds:ArrayCollection = new ArrayCollection();
			//regular clone does not work.... check this one later
			var newCategory:LCCategory = new LCCategory();
			newCategory.key=category.key;
			newCategory.iconsrc=category.iconsrc;
			newCategory.resource=category.resource;
			newCategory.childs=newChilds;
			
			for each (var child:Object in category.childs){
				if(child.hasOwnProperty("requiredAuthority") &&
					child.requiredAuthority!=null){
					if(checkUserForAuthority(child.requiredAuthority)){
						newChilds.addItem(child);
					}
				}else{
					newChilds.addItem(child);
				}
			}
			return newCategory;
		}
	}
}
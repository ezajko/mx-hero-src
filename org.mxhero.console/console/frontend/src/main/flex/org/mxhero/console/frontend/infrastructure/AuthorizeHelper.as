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
			var newCategory:Object;
			for each (var category:Object in list){
				if(category.hasOwnProperty("requiredAuthority") &&
					category.requiredAuthority!=null){
					if(checkUserForAuthority(category.requiredAuthority,category.domainExclusive,category.adminExclusive)){
						newCategory = getCategory(category);
						if((newCategory.childs as ArrayCollection).length>0){
							authorizedList.addItem(newCategory);
						}
					}
				}else{
					newCategory = getCategory(category);
					if((newCategory.childs as ArrayCollection).length>0){
						authorizedList.addItem(newCategory);
					}
				}
			}
			return authorizedList;
		}
		
		public function checkUserForAuthority(authority:String,exclusive:Boolean=false,adminExclusive:Boolean=false,needsOwner:Boolean=false):Boolean{
			if (context==null || 
				context.applicationUser==null){
				return false;
			}
			
			if(exclusive && context.selectedDomain==null){
				return false;
			}
			
			if(needsOwner 
				&& checkAuthority("ROLE_ADMIN") 
				&& context.selectedDomain!=null 
				&& context.selectedDomain.owner==null){
				return false;
			}
			
			if(adminExclusive && context.selectedDomain!=null){
				return false;
			}
			

			return checkAuthority(authority);
		}
		
		public function checkAuthority(authority:String):Boolean{
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
					if(checkUserForAuthority(child.requiredAuthority,child.domainExclusive,child.adminExclusive,child.needsOwner)){
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
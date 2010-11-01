package org.mxhero.console.backend.translator;

import java.util.ArrayList;
import java.util.Collection;

import org.mxhero.console.backend.vo.PageVO;
import org.synyx.hades.domain.Page;

public abstract class AbstractTranslator<V,E> {

	public V translate(E entity){
		if(entity!=null){
			return doTranslate(entity);
		}
		return null;
	}
	
	protected abstract V doTranslate(E entity);
	
	public Collection<V> translate(Collection<E> entityCollection){
		Collection<V> valueCollection = new ArrayList<V>();
		if(entityCollection!=null && !entityCollection.isEmpty()){
			valueCollection = new ArrayList<V>();
			for(E entity : entityCollection){
				valueCollection.add(translate(entity));
			}
		}
		return valueCollection;
	}
	
	public PageVO<V> translate(Page<E> page){
		PageVO<V> pageVO= new PageVO<V>();
		pageVO.setElements(translate(page.asList()));
		pageVO.setHasNextPage(page.hasNextPage());
		pageVO.setHasPreviousPage(page.hasPreviousPage());
		pageVO.setNumber(page.getNumber());
		pageVO.setNumberOfElements(page.getNumberOfElements());
		pageVO.setSize(page.getSize());
		pageVO.setTotalElements(page.getTotalElements());
		pageVO.setTotalPages(page.getTotalPages());
		return pageVO;
	}
}

package org.mxhero.console.backend.translator;

import java.util.ArrayList;
import java.util.Collection;

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
	
}

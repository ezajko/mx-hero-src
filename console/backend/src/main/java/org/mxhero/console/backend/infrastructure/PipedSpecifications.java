package org.mxhero.console.backend.infrastructure;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.synyx.hades.domain.Specification;

public class PipedSpecifications<T> implements Specification<T>{

    private final Specification<T> spec;

    private List<AddedPredicate> addedPredicates = new ArrayList<AddedPredicate>();

    /**
     * Creates a new {@link Specifications} wrapper for the given
     * {@link Specification}.
     * 
     * @param spec
     */
    private PipedSpecifications(Specification<T> spec) {

        this.spec = spec;
    }


    /**
     * Simple static factory method to add some syntactic sugar around a
     * {@link Specification}.
     * 
     * @param <T>
     * @param spec
     * @return
     */
    public static <T> PipedSpecifications<T> where(Specification<T> spec) {

        return new PipedSpecifications<T>(spec);
    }


    /**
     * ANDs the given {@link Specification} to the current one.
     * 
     * @param other
     * @return
     */
    public PipedSpecifications<T> and(final Specification<T> other) {
    	addedPredicates.add(new AddedPredicate(AddType.AND, other));
    	return this;
    }


    /**
     * ORs the given specification to the current one.
     * 
     * @param other
     * @return
     */
    public PipedSpecifications<T> or(final Specification<T> other) {
    	addedPredicates.add(new AddedPredicate(AddType.OR, other));
    	return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.synyx.hades.domain.Specification#toPredicate(javax.persistence.criteria
     * .Root, javax.persistence.criteria.CriteriaQuery,
     * javax.persistence.criteria.CriteriaBuilder)
     */
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
			CriteriaBuilder builder) {
    	
    	Predicate predicate=spec.toPredicate(root, query, builder);
    	for(AddedPredicate addedPredicate : addedPredicates){
    		if(addedPredicate.getType().equals(AddType.AND)){
    			predicate = builder.and(predicate,addedPredicate.getSpecification().toPredicate(root, query, builder));
    		}
    		else if(addedPredicate.getType().equals(AddType.OR)){
    			predicate = builder.or(predicate,addedPredicate.getSpecification().toPredicate(root, query, builder));
    		}
    	}
    	
        return predicate;
    }
	
    private enum AddType{
    	AND,
    	OR;
    }
    
    
    private class AddedPredicate{
    
    	private AddType type;
    	
    	private Specification<T> specification;
    	
		public AddedPredicate(AddType type, Specification<T> specification) {
			super();
			this.type = type;
			this.specification = specification;
		}

		public AddType getType() {
			return type;
		}

		public Specification<T> getSpecification() {
			return specification;
		}
    	
    }


	
}

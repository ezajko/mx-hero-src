package org.mxhero.console.backend.dao.specs;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.mxhero.console.backend.entity.Domain_;
import org.mxhero.console.backend.entity.EmailAccount;
import org.mxhero.console.backend.entity.EmailAccount_;
import org.synyx.hades.domain.Specification;

public class EmailAccountSpecs {

	public static Specification<EmailAccount> nameLike(final String name){
		return new Specification<EmailAccount>() {
			@Override
			public Predicate toPredicate(Root<EmailAccount> root,
					CriteriaQuery<EmailAccount> query, CriteriaBuilder builder) {
				return builder.like(root.get(EmailAccount_.name), "%"+name+"%");
			}
		};
	}

	public static Specification<EmailAccount> lastNameLike(final String lastName){
		return new Specification<EmailAccount>() {
			@Override
			public Predicate toPredicate(Root<EmailAccount> root,
					CriteriaQuery<EmailAccount> query, CriteriaBuilder builder) {
				return builder.like(root.get(EmailAccount_.lastName), "%"+lastName+"%");
			}
		};
	}
	
	public static Specification<EmailAccount> accountLike(final String account){
		return new Specification<EmailAccount>() {
			@Override
			public Predicate toPredicate(Root<EmailAccount> root,
					CriteriaQuery<EmailAccount> query, CriteriaBuilder builder) {
				return builder.like(root.get(EmailAccount_.account), "%"+account+"%");
			}
		};
	}
	
	public static Specification<EmailAccount> domainIdEqual(final Integer id){
		return new Specification<EmailAccount>() {
			@Override
			public Predicate toPredicate(Root<EmailAccount> root,
					CriteriaQuery<EmailAccount> query, CriteriaBuilder builder) {
				query.orderBy(builder.asc(root.get(EmailAccount_.account)));
				return builder.equal(root.get(EmailAccount_.domain).get(Domain_.id), id);
			}
		};
	}

}

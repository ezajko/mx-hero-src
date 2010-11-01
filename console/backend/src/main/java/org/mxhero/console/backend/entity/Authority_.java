package org.mxhero.console.backend.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;


@StaticMetamodel(Authority.class)
public abstract class Authority_ {

	public static volatile SingularAttribute<Authority, Integer> id;
	public static volatile SingularAttribute<Authority, String> authority;

}


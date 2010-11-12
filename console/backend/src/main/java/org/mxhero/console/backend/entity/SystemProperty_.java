package org.mxhero.console.backend.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(SystemProperty.class)
public class SystemProperty_ {

	public static volatile SingularAttribute<SystemProperty, Integer> id;
	public static volatile SingularAttribute<SystemProperty, String> key;
	public static volatile SingularAttribute<SystemProperty, String> value;
	
}

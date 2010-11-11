package org.mxhero.console.backend.entity;

import java.util.Calendar;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(DomainAlias.class)
public class DomainAlias_ {

	public static volatile SingularAttribute<DomainAlias, String> alias;
	public static volatile SingularAttribute<DomainAlias, Domain> domain;
	public static volatile SingularAttribute<DomainAlias, Calendar> creationDate;
	
}

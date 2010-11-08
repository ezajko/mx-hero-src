package org.mxhero.console.backend.entity;

import java.util.Calendar;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;


@StaticMetamodel(Domain.class)
public abstract class Domain_ {

	public static volatile SingularAttribute<Domain, Integer> id;
	public static volatile SingularAttribute<Domain, Calendar> creationDate;
	public static volatile SetAttribute<Domain, EmailAccount> emailAccounts;
	public static volatile SetAttribute<Group, EmailAccount> groups;
	public static volatile SingularAttribute<Domain, Calendar> updatesDate;
	public static volatile SingularAttribute<Domain, ApplicationUser> owner;
	public static volatile SingularAttribute<Domain, String> domain;
	public static volatile SingularAttribute<Domain, String> server;

}


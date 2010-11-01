package org.mxhero.console.backend.entity;

import java.util.Calendar;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;


@StaticMetamodel(EmailAccount.class)
public abstract class EmailAccount_ {

	public static volatile SingularAttribute<EmailAccount, Integer> id;
	public static volatile SingularAttribute<EmailAccount, String> lastName;
	public static volatile SingularAttribute<EmailAccount, String> email;
	public static volatile SingularAttribute<EmailAccount, Calendar> updatedDate;
	public static volatile SingularAttribute<EmailAccount, String> name;
	public static volatile SingularAttribute<EmailAccount, Domain> domain;
	public static volatile SingularAttribute<EmailAccount, Calendar> createdDate;

}


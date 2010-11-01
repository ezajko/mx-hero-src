package org.mxhero.console.backend.entity;

import java.util.Calendar;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;


@StaticMetamodel(ApplicationUser.class)
public abstract class ApplicationUser_ {

	public static volatile SingularAttribute<ApplicationUser, Integer> id;
	public static volatile SingularAttribute<ApplicationUser, String> lastName;
	public static volatile SingularAttribute<ApplicationUser, Boolean> enabled;
	public static volatile SingularAttribute<ApplicationUser, Calendar> creationDate;
	public static volatile SetAttribute<ApplicationUser, Authority> authorities;
	public static volatile SingularAttribute<ApplicationUser, Calendar> lastPasswordUpdate;
	public static volatile SingularAttribute<ApplicationUser, String> notifyEmail;
	public static volatile SingularAttribute<ApplicationUser, String> name;
	public static volatile SingularAttribute<ApplicationUser, String> locale;
	public static volatile SingularAttribute<ApplicationUser, Domain> domain;
	public static volatile SingularAttribute<ApplicationUser, String> userName;
	public static volatile SingularAttribute<ApplicationUser, String> password;

}


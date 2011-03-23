package org.mxhero.console.backend.statistics.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Stat.class)
public class Stat_ {

	public static volatile SingularAttribute<Stat,StatPk> id;
	public static volatile SingularAttribute<Stat,Record> record;
	public static volatile SingularAttribute<Stat,String> value;

}

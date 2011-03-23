package org.mxhero.console.backend.statistics.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Stat.class)
public class StatPk_ {

	public static volatile SingularAttribute<Stat,String> key;
	
	public static volatile SingularAttribute<Stat,RecordPk> recordId;
}

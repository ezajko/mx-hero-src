package org.mxhero.console.backend.statistics.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(StatPk.class)
public class StatPk_ {

	public static volatile SingularAttribute<StatPk,String> key;
	
	public static volatile SingularAttribute<StatPk,RecordPk> recordId;
}

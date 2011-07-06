package org.mxhero.console.backend.statistics.entity;

import java.sql.Timestamp;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(StatPk.class)
public class StatPk_ {

	public static volatile SingularAttribute<StatPk,String> key;
	public static volatile SingularAttribute<RecordPk,Timestamp> insertDate;
	public static volatile SingularAttribute<RecordPk,Long> sequence;
	public static volatile SingularAttribute<Record, String> phase;
	
}

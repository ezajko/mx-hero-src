package org.mxhero.console.backend.statistics.entity;

import java.sql.Timestamp;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
	
@StaticMetamodel(RecordPk.class)
public abstract class RecordPk_ {

	public static volatile SingularAttribute<RecordPk,Timestamp> insertDate;
	public static volatile SingularAttribute<RecordPk,Long> sequence;
}

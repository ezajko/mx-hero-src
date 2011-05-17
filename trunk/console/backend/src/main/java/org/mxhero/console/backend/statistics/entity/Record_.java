package org.mxhero.console.backend.statistics.entity;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Record.class)
public abstract class Record_ {

	public static volatile SingularAttribute<Record,RecordPk> id;
	public static volatile SingularAttribute<Record,String> messageId;
	public static volatile SingularAttribute<Record,Timestamp> parentInsertDate;
	public static volatile SingularAttribute<Record,Long> parentSequence;
	public static volatile SingularAttribute<Record, String> phase;
	public static volatile SingularAttribute<Record, String> sender;
	public static volatile SingularAttribute<Record, String> senderId;
	public static volatile SingularAttribute<Record, String> recipient;
	public static volatile SingularAttribute<Record, String> recipientId;
	public static volatile SingularAttribute<Record, String> senderDomainId;
	public static volatile SingularAttribute<Record, String> recipientDomainId;
	public static volatile SingularAttribute<Record, String> subject;
	public static volatile SingularAttribute<Record, String> from;
	public static volatile SingularAttribute<Record, String> toRecipients;
	public static volatile SingularAttribute<Record, String> ccRecipients;
	public static volatile SingularAttribute<Record, String> bccRecipients;
	public static volatile SingularAttribute<Record, String> ngRecipients;
	public static volatile SingularAttribute<Record, Calendar> sentDate;
	public static volatile SingularAttribute<Record, Integer> bytesSize;
	public static volatile SingularAttribute<Record, String> state;
	public static volatile SingularAttribute<Record, String> stateReason;
	public static volatile SingularAttribute<Record, String> flow;
	public static volatile SetAttribute<Record, Stat> stats;

}

package org.mxhero.engine.plugin.statistics.internal.repository;

import java.util.ArrayList;
import java.util.Collection;

import javax.sql.DataSource;

import org.mxhero.engine.plugin.statistics.internal.entity.Record;
import org.mxhero.engine.plugin.statistics.internal.entity.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Repository("jdbcRecordRepository")
public class JdbcRecordRepository implements RecordRepository{

	 
	private final TransactionTemplate transactionTemplate;
	
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcRecordRepository(DataSource ds, TransactionTemplate transactionTemplate){
		this.template = new NamedParameterJdbcTemplate(ds);
		this.transactionTemplate = transactionTemplate;
	}
	
	public void saveRecord(final Collection<Record> records) {
		final String sql = "INSERT INTO statistics.mail_records (`insert_date`,`record_sequence`,`server_name`,`bcc_recipients`" +
				",`bytes_size`,`cc_recipients`,`from_recipients`,`message_id`,`ng_recipients`" +
				",`phase`,`recipient`,`recipient_domain_id`,`recipient_id`,`sender`" +
				",`sender_domain_id`,`sender_id`,`state`,`state_reason`,`subject`,`to_recipients`,`flow`,`sender_group`,`recipient_group`) " +
				" VALUES( :insert_date,:record_sequence,:server_name,:bcc_recipients,:bytes_size,:cc_recipients,:from_recipients" +
				",:message_id,:ng_recipients,:phase,:recipient,:recipient_domain_id,:recipient_id" +
				",:sender,:sender_domain_id,:sender_id,:state,:state_reason,:subject,:to_recipients,:flow,:sender_group,:recipient_group)" +
				" ON DUPLICATE KEY UPDATE `bcc_recipients`=VALUES(bcc_recipients),`bytes_size`=VALUES(bytes_size)" +
				",`cc_recipients`=VALUES(cc_recipients),`from_recipients`=VALUES(from_recipients),`ng_recipients`=VALUES(ng_recipients)" +
				",`phase`=VALUES(phase),`recipient`=VALUES(recipient)" +
				",`recipient_domain_id`=VALUES(recipient_domain_id),`recipient_id`=VALUES(recipient_id),`sender`=VALUES(sender)" +
				",`sender_domain_id`=VALUES(sender_domain_id),`sender_id`=VALUES(sender_id)" +
				",`state`=VALUES(state),`state_reason`=VALUES(state_reason),`subject`=VALUES(subject)" +
				",`to_recipients`=VALUES(to_recipients),`flow`=VALUES(flow),`sender_group`=VALUES(sender_group),`recipient_group`=VALUES(recipient_group);";
		if(records!=null && records.size()>0){
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
				protected void doInTransactionWithoutResult(TransactionStatus arg0) {
					template.batchUpdate(sql, recordsToBatch(records));
				}
			});
		}
	}

	public void saveRecord(Record record) {
		if(record!=null){
			Collection<Record> records = new ArrayList<Record>();
			records.add(record);
			saveRecord(records);
		}
	}

	public void saveStat(final Collection<Stat> stats) {
		final String sql = "INSERT INTO statistics.mail_stats (`stat_key`,`stat_value`,`insert_date`,`record_sequence`,`server_name`,`phase`) " +
				" VALUES(:stat_key,:stat_value,:insert_date,:record_sequence,:server_name,:phase) " +
				" ON DUPLICATE KEY UPDATE `stat_value`=VALUES(stat_value);";
		if(stats!=null && stats.size()>0){
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
				protected void doInTransactionWithoutResult(TransactionStatus arg0) {
					template.batchUpdate(sql, statsToBatch(stats));
				}
			});			
		}
	}

	public void saveStat(Stat stat) {
		if(stat!=null){
			Collection<Stat> stats = new ArrayList<Stat>();
			stats.add(stat);
			saveStat(stats);
		}
	}
	
	private SqlParameterSource[] recordsToBatch(Collection<Record> records){
		SqlParameterSource batchValues[] = new SqlParameterSource[records.size()];
		int i = 0;
		for(Record record : records){
			MapSqlParameterSource source = new MapSqlParameterSource();
			source.addValue("insert_date", record.getInsertDate());
			source.addValue("record_sequence", record.getSequence());
			source.addValue("server_name", record.getServerName());
			source.addValue("bcc_recipients", record.getBccRecipients());
			source.addValue("bytes_size", record.getBytesSize());
			source.addValue("cc_recipients", record.getCcRecipients());
			source.addValue("from_recipients", record.getFrom());
			source.addValue("message_id", record.getMessageId());
			source.addValue("ng_recipients", record.getNgRecipients());
			source.addValue("phase", record.getPhase());
			source.addValue("recipient", record.getRecipient());
			source.addValue("recipient_domain_id", record.getRecipientDomainId());
			source.addValue("recipient_id", record.getRecipientId());
			source.addValue("sender", record.getSender());
			source.addValue("sender_domain_id", record.getSenderDomainId());
			source.addValue("sender_id", record.getSenderId());
			source.addValue("state", record.getState());
			source.addValue("state_reason", record.getStateReason());
			source.addValue("subject", record.getSubject());
			source.addValue("to_recipients", record.getToRecipients());
			source.addValue("flow", record.getFlow());
			source.addValue("sender_group", record.getSenderGroup());
			source.addValue("recipient_group", record.getRecipientGroup());
			batchValues[i]=source;
			++i;
		}
		
		return batchValues;
	}
	
	private SqlParameterSource[] statsToBatch(Collection<Stat> stats){
		SqlParameterSource batchValues[] = new SqlParameterSource[stats.size()];
		int i = 0;

		for(Stat stat : stats){
			MapSqlParameterSource source = new MapSqlParameterSource();
			source.addValue("stat_key", stat.getKey());
			source.addValue("stat_value", stat.getValue());
			source.addValue("insert_date", stat.getInsertDate());
			source.addValue("record_sequence", stat.getSequence());
			source.addValue("server_name", stat.getServerName());
			source.addValue("phase", stat.getPhase());
			batchValues[i]=source;
			++i;
		}
		
		return batchValues;
	}
	
}

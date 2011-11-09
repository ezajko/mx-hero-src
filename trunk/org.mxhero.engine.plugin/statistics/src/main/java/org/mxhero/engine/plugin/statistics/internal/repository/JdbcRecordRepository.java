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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("jdbcRecordRepository")
public class JdbcRecordRepository implements RecordRepository{

	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcRecordRepository(DataSource ds){
		this.template = new NamedParameterJdbcTemplate(ds);
	}
	
	@Override
	@Transactional(readOnly=false,propagation=Propagation.REQUIRES_NEW, isolation=Isolation.SERIALIZABLE)
	public void saveRecord(final Collection<Record> records) {
		String sql = "INSERT INTO mail_records (`insert_date`,`record_sequence`,`bcc_recipeints`" +
				",`bytes_size`,`cc_recipeints`,`from_recipeints`,`message_id`,`ng_recipeints`" +
				",`phase`,`recipient`,`recipient_domain_id`,`recipient_id`,`sender`" +
				",`sender_domain_id`,`sender_id`,`sent_date`,`state`,`state_reason`,`subject`,`to_recipeints`,`flow`) " +
				" VALUES( :insert_date,:record_sequence,:bcc_recipeints,:bytes_size,:cc_recipeints,:from_recipeints" +
				",:message_id,:ng_recipeints,:phase,:recipient,:recipient_domain_id,:recipient_id" +
				",:sender,:sender_domain_id,:sender_id,:sent_date,:state,:state_reason,:subject,:to_recipeints,:flow)" +
				" ON DUPLICATE KEY UPDATE `bcc_recipeints`=VALUES(bcc_recipeints),`bytes_size`=VALUES(bytes_size)" +
				",`cc_recipeints`=VALUES(cc_recipeints),`from_recipeints`=VALUES(from_recipeints),`ng_recipeints`=VALUES(ng_recipeints)" +
				",`phase`=VALUES(phase),`recipient`=VALUES(recipient)" +
				",`recipient_domain_id`=VALUES(recipient_domain_id),`recipient_id`=VALUES(recipient_id),`sender`=VALUES(sender)" +
				",`sender_domain_id`=VALUES(sender_domain_id),`sender_id`=VALUES(sender_id),`sent_date`=VALUES(sent_date)" +
				",`state`=VALUES(state),`state_reason`=VALUES(state_reason),`subject`=VALUES(subject)" +
				",`to_recipeints`=VALUES(to_recipeints),`flow`=VALUES(flow);";
		if(records!=null && records.size()>0){
			template.batchUpdate(sql, recordsToBatch(records));
		}
	}

	@Override
	public void saveRecord(Record record) {
		if(record!=null){
			Collection<Record> records = new ArrayList<Record>();
			records.add(record);
			saveRecord(records);
		}
	}

	@Override
	@Transactional(readOnly=false,propagation=Propagation.REQUIRES_NEW, isolation=Isolation.SERIALIZABLE)
	public void saveStat(Collection<Stat> stats) {
		String sql = "INSERT INTO mail_stats (`stat_key`,`stat_value`,`insert_date`,`record_sequence`,`phase`) " +
				" VALUES(:stat_key,:stat_value,:insert_date,:record_sequence,:phase) " +
				" ON DUPLICATE KEY UPDATE `stat_value`=VALUES(stat_value);";
		if(stats!=null && stats.size()>0){
			template.batchUpdate(sql, statsToBatch(stats));
		}
	}

	@Override
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
			source.addValue("bcc_recipeints", record.getBccRecipients());
			source.addValue("bytes_size", record.getBytesSize());
			source.addValue("cc_recipeints", record.getCcRecipients());
			source.addValue("from_recipeints", record.getFrom());
			source.addValue("message_id", record.getMessageId());
			source.addValue("ng_recipeints", record.getNgRecipients());
			source.addValue("phase", record.getPhase());
			source.addValue("recipient", record.getRecipient());
			source.addValue("recipient_domain_id", record.getRecipientDomainId());
			source.addValue("recipient_id", record.getRecipientId());
			source.addValue("sender", record.getSender());
			source.addValue("sender_domain_id", record.getSenderDomainId());
			source.addValue("sender_id", record.getSenderId());
			source.addValue("sent_date", record.getSentDate());
			source.addValue("state", record.getState());
			source.addValue("state_reason", record.getStateReason());
			source.addValue("subject", record.getSubject());
			source.addValue("to_recipeints", record.getToRecipients());
			source.addValue("flow", record.getFlow());
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
			source.addValue("phase", stat.getPhase());
			batchValues[i]=source;
			++i;
		}
		
		return batchValues;
	}
	
}

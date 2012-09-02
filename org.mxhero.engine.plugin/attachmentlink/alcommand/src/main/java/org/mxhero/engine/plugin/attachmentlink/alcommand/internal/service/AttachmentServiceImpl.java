package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.service;

import java.util.List;

import javax.sql.DataSource;

import org.mxhero.engine.plugin.attachmentlink.alcommand.service.AttachmentService;
import org.mxhero.engine.plugin.attachmentlink.alcommand.service.TransactionAttachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

/**
 * The Class AttachmentServiceImpl.
 */
@Repository(value = "attachmentLinkExternalService")
public class AttachmentServiceImpl implements AttachmentService {
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(AttachmentServiceImpl.class);
	
	/** The jdbc. */
	private NamedParameterJdbcTemplate jdbc;
	
	/**
	 * Instantiates a new attachment service impl.
	 *
	 * @param ds the ds
	 */
	@Autowired
	public AttachmentServiceImpl(DataSource ds){
		this.jdbc = new NamedParameterJdbcTemplate(ds);
	}

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.attachmentlink.alcommand.service.AttachmentService#getTransactionToProcess(java.lang.Integer)
	 */
	@Override
	public List<TransactionAttachment> getTransactionToProcess(Integer limit) {
		logger.debug("Getting {} attachment to synchronize", limit);
		String query = "SELECT " +
				"ex.message_attach_ex_storage_id as idMessageAttach, " +
				"ex.email_to_synchro as email, " +
				"ex.is_sender as isSender, " +
				"ex.is_recipient as isRecipient, " +
				"a.path as filePath, " +
				"a.file_name as originalFileName, " +
				"msg.email_date as emailDate, " +
				"msg.sender_email as sender, " +
				"m.recipient_email as recipient, " +
				"msg.subject as subject " +
				"from attachments.message_attach_ex_storage ex " +
				"inner join attachments.message_attach m on m.message_attach_id = ex.message_attach_id " +
				"inner join attachments.attach a on a.attach_id = m.attach_id " +
				"inner join attachments.message msg on msg.message_id = m.message_id " + 
				"where ex.was_proccessed = false " +
				"order by m.creation_date asc " +
				"limit :limit";
		List<TransactionAttachment> result = jdbc.query(query, new MapSqlParameterSource("limit", limit), new BeanPropertyRowMapper<TransactionAttachment>(TransactionAttachment.class));
		logger.debug("Returning {} attachments to synchronize", result.size());
		return result;
				
	}

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.attachmentlink.alcommand.service.AttachmentService#sendMessage(org.mxhero.engine.plugin.attachmentlink.alcommand.service.TransactionAttachment, boolean)
	 */
	@Override
	public void sendMessage(TransactionAttachment tx, boolean result) {
		if(result){
			logger.debug("Mark attachment {} as proccessed",tx);
			String sql = "UPDATE attachments.message_attach_ex_storage " +
					"set attach_cloud_url = :publicUrl, was_proccessed = true " +
					"where message_attach_ex_storage_id = :idMessageAttach";
			SqlParameterSource paramMap = new BeanPropertySqlParameterSource(tx);
			jdbc.update(sql, paramMap);
		}
	}

}

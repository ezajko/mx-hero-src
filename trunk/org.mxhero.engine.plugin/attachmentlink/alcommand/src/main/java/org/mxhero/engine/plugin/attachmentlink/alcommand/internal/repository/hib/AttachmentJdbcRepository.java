/**
 * 
 */
package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.repository.hib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.Attach;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.Message;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.MessageAttachRecipient;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.repository.AttachmentRepository;
import org.mxhero.engine.plugin.storageapi.UserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * @author royojp
 *
 */
@Repository(value = "jdbcRepository")
public class AttachmentJdbcRepository implements AttachmentRepository {
	
	private static Logger log = Logger.getLogger(AttachmentJdbcRepository.class);
	
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public AttachmentJdbcRepository(@Qualifier(value = "dataSource")DataSource ds){
		this.template = new NamedParameterJdbcTemplate(ds);
	}


	/**
	 * 
	 */
	public AttachmentJdbcRepository() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.attachmentlink.repository.AttachmentRepository#getAttachForChecksum(org.mxhero.engine.plugin.attachmentlink.domain.Attach)
	 */
	@Override
	public Attach getAttachForChecksum(Attach attach) {
		String sql = "select a.attach_id as id, a.md5_checksum as md5Checksum, a.file_name as file_name,a.size as size,a.mime_type as mimeType, a.path as path from attachments.attach a where a.md5_checksum = :checksum";
		List<Attach> queryForList = template.query(sql, new MapSqlParameterSource("checksum", attach.getMd5Checksum()), new BeanPropertyRowMapper<Attach>(Attach.class));
		if(queryForList.isEmpty()){
			return null;
		}else{
			return queryForList.get(0);
		}
	}

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.attachmentlink.repository.AttachmentRepository#getAttachmentsForEmail(org.mxhero.engine.plugin.attachmentlink.domain.Message)
	 */
	@Override
	public List<Attach> getAttachmentsForEmail(Message mail) {
		String sql = "select a.attach_id as id, a.md5_checksum as md5Checksum, a.file_name as file_name,a.size as size,a.mime_type as mimeType, a.path as path from attachments.attach a inner join message_attach ma on a.attach_id = ma.attach_id inner join message m on m.message_id = ma.message_id where m.message_platform_id = :id";
		return template.query(sql, new MapSqlParameterSource("id", mail.getMessagePlatformId()), new BeanPropertyRowMapper<Attach>(Attach.class));
	}

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.attachmentlink.repository.AttachmentRepository#hasBeenProcessed(org.mxhero.engine.plugin.attachmentlink.domain.Message)
	 */
	@Override
	public boolean hasBeenProcessed(Message mail) {
		String sql = "select processed from attachments.message where message_platform_id = :id";
		List<Boolean> queryForList = template.queryForList(sql, new MapSqlParameterSource("id", mail.getMessagePlatformId()), Boolean.class);
		if(queryForList.isEmpty())return false;
		return queryForList.get(0);
	}

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.attachmentlink.repository.AttachmentRepository#save(org.mxhero.engine.plugin.attachmentlink.domain.Message)
	 */
	@Override
	public Message save(Message attach) {
		Message toReturn = null;
		Long isToSave = isToSave(attach);
		try {
			attach.setProcessed(true);
			if(isToSave==null){
				saveNewMessage(attach);					
			}else{
				attach.setId(isToSave);
				updateMessage(attach);
			}
			saveOrUpdateAttachments(attach,attach.getMessageAttachRecipient());
			toReturn = attach;
		} catch (DataIntegrityViolationException e) {
			log.warn("Two or more threads became at the same time together. Requeueing....",e);
			attach.requeueMessage();
		}
		return toReturn;
	}


	private void updateMessage(Message attach) {
		String insertMsg = "update attachments.message set message_platform_id = :msgId,sender_email = :sender,process_ack_download = :prAck,msg_ack_download = :msgAck,msg_ack_download_html = :msgAckHtml,subject = :sub where message_id = :id";
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("msgId", attach.getMessagePlatformId());
		values.put("sender", attach.getSender());
		values.put("prAck", attach.getProcessAckDownloadMail());
		values.put("msgAck", attach.getMessageAckDownloadMail());
		values.put("msgAckHtml", attach.getMessageAckDownloadMailHtml());
		values.put("sub", attach.getSubject());
		values.put("id", attach.getId());
		SqlParameterSource params = new MapSqlParameterSource(values);
		template.update(insertMsg, params);
	}


	private void saveNewMessage(Message attach) {
		String insertMsg = "insert into attachments.message (message_platform_id,sender_email,process_ack_download,msg_ack_download,msg_ack_download_html,subject) values (:msgPlatId,:sendEmail,:pAck,:msgAck,:msgAckHtml,:sub)";
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("msgPlatId", attach.getMessagePlatformId());
		values.put("sendEmail", attach.getSender());
		values.put("pAck", attach.getProcessAckDownloadMail());
		values.put("msgAck", attach.getMessageAckDownloadMail());
		values.put("msgAckHtml", attach.getMessageAckDownloadMailHtml());
		values.put("sub", attach.getSubject());
		SqlParameterSource params = new MapSqlParameterSource(values);
		KeyHolder key = new GeneratedKeyHolder();
		template.update(insertMsg, params, key);
		attach.setId(key.getKey().longValue());
	}


	private void saveOrUpdateAttachments(Message msg,Set<MessageAttachRecipient> messageAttachRecipient) {
		for (MessageAttachRecipient msgAttach : messageAttachRecipient) {
			msgAttach.setMessage(msg);
			saveOrUpdateAttach(msgAttach);
			saveOrUpdateMessageAttach(msgAttach);
		}
		
	}


	private void saveOrUpdateMessageAttach(MessageAttachRecipient msgAttach) {
		Long attachExist = msgAttachExist(msgAttach);
		if(attachExist==null){
			saveMessageAttach(msgAttach);
		}else{
			msgAttach.setId(attachExist);
		}
	}


	private void saveMessageAttach(MessageAttachRecipient attach) {
		String sql = "insert into attachments.message_attach (message_id,attach_id,recipient_email) values(:msg,:att,:recip)";
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("msg", attach.getMessage().getId());
		values.put("att", attach.getAttach().getId());
		values.put("recip", attach.getRecipient());
		SqlParameterSource params = new MapSqlParameterSource(values);
		KeyHolder key = new GeneratedKeyHolder();
		template.update(sql, params, key);
		attach.setId(key.getKey().longValue());
		saveMessageAttachStorage(attach);
	}


	private void saveMessageAttachStorage(MessageAttachRecipient attach) {
		UserResult recipient = attach.getMessage().getResultCloudStorageRecipient();
		if(recipient!=null){
			saveMessageAttachStorage(attach, recipient.getEmail());
		}
		UserResult sender = attach.getMessage().getResultCloudStorageSender();
		if(sender!=null){
			saveMessageAttachStorage(attach, sender.getEmail());
		}
	}


	private void saveMessageAttachStorage(MessageAttachRecipient attach, String email) {
		String sql = "insert into attachments.message_attach_ex_storage (message_attach_id, email_to_synchro) values(:msg,:email)";
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("msg", attach.getId());
		values.put("email", email);
		SqlParameterSource params = new MapSqlParameterSource(values);
		template.update(sql, params);
	}


	private Long msgAttachExist(MessageAttachRecipient attach) {
		String sql = "select message_attach_id from attachments.message_attach where message_id = :msg and attach_id = :att and recipient_email = :rec";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msg", attach.getMessage().getId());
		map.put("att", attach.getAttach().getId());
		map.put("rec", attach.getRecipient());
		SqlParameterSource values = new MapSqlParameterSource(map);
		List<Long> queryForList = template.queryForList(sql, values,Long.class);
		if(queryForList.isEmpty())return null;
		else return queryForList.get(0);
	}


	private void saveOrUpdateAttach(MessageAttachRecipient msgAttach) {
		Long attachExist = attachExist(msgAttach.getAttach());
		if(attachExist!=null){
			msgAttach.getAttach().setId(attachExist);
			updateAttach(msgAttach.getAttach());
		}else{
			saveAttach(msgAttach.getAttach());
		}
	}


	private void saveAttach(Attach attach) {
		String sql = "insert into attachments.attach (md5_checksum,file_name,size,mime_type,path) values(:md5,:file,:size,:mime,:path)";
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("md5", attach.getMd5Checksum());
		values.put("file", attach.getFileName());
		values.put("size", attach.getSize());
		values.put("mime", attach.getMimeType());
		values.put("path", attach.getPath());
		SqlParameterSource params = new MapSqlParameterSource(values);
		KeyHolder key = new GeneratedKeyHolder();
		template.update(sql, params, key);
		attach.setId(key.getKey().longValue());
	}


	private void updateAttach(Attach attach) {
		String sql = "update attachments.attach set md5_checksum = :md5, file_name = :file,size = :size ,mime_type = :mime,path = :path where attach_id = :id";
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("md5", attach.getMd5Checksum());
		values.put("file", attach.getFileName());
		values.put("size", attach.getSize());
		values.put("mime", attach.getMimeType());
		values.put("path", attach.getPath());
		values.put("id", attach.getId());
		SqlParameterSource params = new MapSqlParameterSource(values);
		template.update(sql, params);
	}


	private Long attachExist(Attach attach) {
		String sql = "select attach_id from attachments.attach where md5_checksum = :checksum";
		List<Long> queryForList = template.queryForList(sql, new MapSqlParameterSource("checksum", attach.getMd5Checksum()),Long.class);
		if(queryForList.isEmpty()){
			return null;
		}else{
			return queryForList.get(0);
		}
	}


	private Long isToSave(Message attach) {
		String sql = "select message_id from attachments.message where message_platform_id = :id";
		List<Long> queryForInt = template.queryForList(sql, new MapSqlParameterSource("id", attach.getMessagePlatformId()),Long.class);
		if(queryForInt.isEmpty()){
			return null;
		}else {
			return queryForInt.get(0);
		}
	}

}

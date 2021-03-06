/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.engine.plugin.attachmentlink.fileserver.dbaccess.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.mxhero.engine.plugin.attachmentlink.fileserver.dbaccess.AttachmentRepository;
import org.mxhero.engine.plugin.attachmentlink.fileserver.domain.ContentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcRepository implements AttachmentRepository {
	
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcRepository(DataSource ds){
		this.template = new NamedParameterJdbcTemplate(ds);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void accessFirstTime(Long idMessageAttach) {
		String sql = "update message_attach set was_access_first_time = 1 where message_attach_id = :id";
		Map paramMap = new HashMap();
		paramMap.put("id", idMessageAttach);
		template.update(sql, paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ContentDTO getContent(Long idMessageAttachment) {
		String sql = "select m.message_id as idMessage, m.message_attach_id as idMessageAttach, msg.subject as subject, msg.message_platform_id as messageId, msg.process_ack_download as processMsg, msg.msg_ack_download as msgMail, msg.msg_ack_download_html as msgMailHtml, msg.sender_email as senderMail, m.recipient_email as recipientMail, m.was_access_first_time as accessed, a.size as length, a.path as path, a.file_name as fileName, a.mime_type as contentType from message_attach m inner join attach a on m.attach_id = a.attach_id inner join message msg on msg.message_id = m.message_id where m.message_attach_id = :id";
		Map paramMap = new HashMap();
		paramMap.put("id", idMessageAttachment);
		return template.queryForObject(sql, paramMap, new BeanPropertyRowMapper<ContentDTO>(ContentDTO.class));
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isAllowed(Long idMessageAttach) {
		String sql = "select enable_to_download from message_attach where message_attach_id = :id";
		Map paramMap = new HashMap();
		paramMap.put("id", idMessageAttach);
		return template.queryForObject(sql, paramMap, Boolean.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void saveHistory(Long idMessageAttachment, boolean success) {
		String sql = "insert into history_access_attach (message_attach_id,could_download) values (:id,:could)";
		Map paramMap = new HashMap();
		paramMap.put("id", idMessageAttachment);
		paramMap.put("could", success);
		template.update(sql, paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void unsubscribe(Long messageId){
		String sql = "update message_attach set was_access_first_time = 1 where message_id = :messageId ";
		Map paramMap = new HashMap();
		paramMap.put("messageId", messageId);
		template.update(sql, paramMap);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContentDTO> getContentList(Long messageId, String recipient) {
		String sql = "select m.message_id as idMessage, m.message_attach_id as idMessageAttach, msg.subject as subject, msg.message_platform_id as messageId, msg.process_ack_download as processMsg, msg.msg_ack_download as msgMail, msg.msg_ack_download_html as msgMailHtml, msg.sender_email as senderMail, m.recipient_email as recipientMail, m.was_access_first_time as accessed, a.size as length, a.path as path, a.file_name as fileName, a.mime_type as contentType from message_attach m inner join attach a on m.attach_id = a.attach_id inner join message msg on msg.message_id = m.message_id where msg.message_id = :messageId and m.recipient_email = :recipient and enable_to_download = 1";
		MapSqlParameterSource paramSource = new MapSqlParameterSource("messageId", messageId);
		paramSource.addValue("recipient", recipient);
		return template.query(sql, paramSource,new BeanPropertyRowMapper<ContentDTO>(ContentDTO.class));
	}
	
	@Override
	public String getPulicUrl(Long idMessageAttach, String email) {
		String sql = "select m.attach_cloud_url as url" +
				"from message_attach_ex_storage " +
				"where email_to_synchro = :email and message_attach_id = :idMessage";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("idMessage", idMessageAttach);
		params.addValue("email", email);
		List<String> query = template.query(sql, params, new SingleColumnRowMapper<String>(String.class));
		if(query.isEmpty())return null;
		return query.get(0);
	}
}

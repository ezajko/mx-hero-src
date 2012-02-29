package org.mxhero.engine.plugin.threadlight.internal.repository.jdbc;

import java.util.HashSet;
import java.util.List;

import javax.sql.DataSource;

import org.mxhero.engine.plugin.threadlight.internal.repository.ThreadRowRepository;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRowFollower;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRowPk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("jdbcRepository")
public class JdbcThreadRowRepository implements ThreadRowRepository{

	private static Logger log = LoggerFactory.getLogger(JdbcThreadRowRepository.class);
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcThreadRowRepository(DataSource ds) {
		this.template = new NamedParameterJdbcTemplate(ds);
	}

	@Override
	@Transactional(readOnly=true)
	public ThreadRow find(ThreadRowPk pk) {
		ThreadRow threadRow = null;
		String sql = " SELECT * FROM `"+ThreadRowMapper.DATABASE+"`.`"+ThreadRowMapper.TABLE_NAME+"`"
					+" WHERE `"+ThreadRowMapper.MESSAGE_ID+"` = :messageId " 
					+" AND `"+ThreadRowMapper.SENDER_MAIL+"` = :senderMail " 
					+" AND `"+ThreadRowMapper.RECIPIENT_MAIL+"` = :recipientMail ;";
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("messageId", pk.getMessageId());
		source.addValue("senderMail", pk.getSenderMail());
		source.addValue("recipientMail", pk.getRecipientMail());
		List<ThreadRow> rowResults = template.query(sql, source, new ThreadRowMapper());
		if(rowResults!=null && rowResults.size()>0){
			threadRow = rowResults.get(0);
			List<ThreadRowFollower> followresResult = findFollowers(threadRow.getId());
			if(followresResult!=null){
				threadRow.setFollowers(new HashSet<ThreadRowFollower>());
				for(ThreadRowFollower follower : followresResult){
					follower.setThreadRow(threadRow);
					threadRow.getFollowers().add(follower);
				}
			}
		}
		return threadRow;
	}

	private List<ThreadRowFollower> findFollowers(Long threadRowId){
		String sql = " SELECT `"+ThreadRowFollowerMapper.FOLLOWER+"`,  `"+ThreadRowFollowerMapper.PARAMETERS+"`" +
					" FROM `"+ThreadRowFollowerMapper.DATABASE+"`.`"+ThreadRowFollowerMapper.TABLE_NAME+"`" +
					" WHERE `"+ThreadRowFollowerMapper.THREAD_ID+"` = :threadId ;";
		return template.query(sql, new MapSqlParameterSource("threadId",threadRowId), new ThreadRowFollowerMapper());
	}
	
	
	@Override
	@Transactional(readOnly=false)
	public Long saveThread(ThreadRow threadRow) {
		log.trace("saving "+threadRow);
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("messageId", threadRow.getPk().getMessageId());
		source.addValue("recipientMail", threadRow.getPk().getRecipientMail());
		source.addValue("senderMail", threadRow.getPk().getSenderMail());
		Long id = null;
		ThreadRow baseRecord = find(threadRow.getPk());
		if(baseRecord==null){
			log.trace("inserting "+threadRow);
			String sql = "INSERT INTO `"+ThreadRowMapper.DATABASE+"`.`"+ThreadRowMapper.TABLE_NAME+"` " +
					" (`"+ThreadRowMapper.CREATION_TIME+"`,`"+ThreadRowMapper.MESSAGE_ID+"`,`"+ThreadRowMapper.RECIPIENT_MAIL+"`,`"+ThreadRowMapper.SENDER_MAIL+"`,`"+ThreadRowMapper.SUBJECT+"`,`"+ThreadRowMapper.REPLY_TIME+"`,`"+ThreadRowMapper.SNOOZE_TIME+"`) " +
					" VALUES(:creationTime,:messageId,:recipientMail,:senderMail,:subject,:replyTime,:snoozeTime) ;";
			source.addValue("subject", threadRow.getSubject());
			source.addValue("replyTime", threadRow.getReplyTime());
			source.addValue("snoozeTime", threadRow.getSnoozeTime());
			source.addValue("creationTime", threadRow.getCreationTime());
			KeyHolder threadRowId = new GeneratedKeyHolder();
			log.trace("inserted "+template.update(sql, source,threadRowId));
			id = threadRowId.getKey().longValue();
		}else{
			log.trace("updating "+threadRow);
			id=baseRecord.getId();
			MapSqlParameterSource updateSource = new MapSqlParameterSource();
			updateSource.addValue("replyTime", threadRow.getReplyTime());
			updateSource.addValue("snoozeTime", threadRow.getSnoozeTime());
			updateSource.addValue("id", id);
			String sql = "UPDATE `"+ThreadRowMapper.DATABASE+"`.`"+ThreadRowMapper.TABLE_NAME+"` " +
				" SET `"+ThreadRowMapper.REPLY_TIME+"` = :replyTime, `"+ThreadRowMapper.SNOOZE_TIME+"` = :snoozeTime " +
				" WHERE `"+ThreadRowMapper.ID+"` = :id";
			log.trace("updated "+template.update(sql, updateSource));
			
		}
		log.trace("id for "+threadRow+" is "+id);
		return id;
	}

	@Override
	@Transactional(readOnly=false)
	public void addFollower(ThreadRow threadRow, ThreadRowFollower follower) {
		String sql = " INSERT INTO `"+ThreadRowFollowerMapper.DATABASE+"`.`"+ThreadRowFollowerMapper.TABLE_NAME+"` " +
				" (`"+ThreadRowFollowerMapper.THREAD_ID+"`,`"+ThreadRowFollowerMapper.FOLLOWER+"`,`"+ThreadRowFollowerMapper.PARAMETERS+"`) "+
				" VALUES(:threadId,:follower,:parameters) " +
				" ON DUPLICATE KEY UPDATE " +
				" `"+ThreadRowFollowerMapper.FOLLOWER+"`=VALUES("+ThreadRowFollowerMapper.FOLLOWER+")," +
				" `"+ThreadRowFollowerMapper.PARAMETERS+"`=VALUES("+ThreadRowFollowerMapper.PARAMETERS+");";
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("threadId", threadRow.getId());
		source.addValue("follower", follower.getFollower());
		source.addValue("parameters", follower.getFolowerParameters());
		template.update(sql, source);
	}

	@Override
	@Transactional(readOnly=false)
	public void removeFollower(ThreadRow threadRow, String follower) {
		String sql = " DELETE FROM `"+ThreadRowFollowerMapper.DATABASE+"`.`"+ThreadRowFollowerMapper.TABLE_NAME+"` " +
					" WHERE `"+ThreadRowFollowerMapper.THREAD_ID+"` = :threadId AND `"+ThreadRowFollowerMapper.FOLLOWER+"` = :follower ;";
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("threadId", threadRow.getId());
		source.addValue("follower", follower);
		template.update(sql, source);
		
		String removeUnfollowedThread = "DELETE FROM `"+ThreadRowMapper.DATABASE+"`.`"+ThreadRowMapper.TABLE_NAME+"` " +
				" WHERE "+ThreadRowMapper.ID+" = :id " +
				" AND NOT EXISTS (SELECT 1 FROM `"+ThreadRowFollowerMapper.DATABASE+"`.`"+ThreadRowFollowerMapper.TABLE_NAME+"` " +
				" WHERE "+ThreadRowFollowerMapper.THREAD_ID+" = :threadId);";
		MapSqlParameterSource threadSource = new MapSqlParameterSource("id",threadRow.getId());
		threadSource.addValue("threadId", threadRow.getId());
		template.update(removeUnfollowedThread,threadSource);
	}

}

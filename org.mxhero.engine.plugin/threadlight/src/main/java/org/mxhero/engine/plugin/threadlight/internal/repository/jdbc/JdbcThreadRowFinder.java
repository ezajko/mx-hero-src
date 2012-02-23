package org.mxhero.engine.plugin.threadlight.internal.repository.jdbc;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.mxhero.engine.plugin.threadlight.internal.repository.ThreadRowFinder;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRowFollower;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRowPk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("jdbcFinder")
@Transactional(readOnly=true)
public class JdbcThreadRowFinder implements ThreadRowFinder{

	private NamedParameterJdbcTemplate template;
	private static final String SQL = " SELECT * FROM "+ThreadRowMapper.DATABASE+"."+ThreadRowMapper.TABLE_NAME+" ";
	
	@Autowired
	public JdbcThreadRowFinder(DataSource ds) {
		this.template = new NamedParameterJdbcTemplate(ds);
	}

	public Map<ThreadRowPk, ThreadRow> findBySpecsMap(Timestamp since) {
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("snoozeSince",since);
		source.addValue("creationSince",since);
		
		
		String sql = SQL + " WHERE ("+ThreadRowMapper.SNOOZE_TIME+" IS NOT NULL AND "+ThreadRowMapper.SNOOZE_TIME+" >= :snoozeSince) " +
				" OR ("+ThreadRowMapper.SNOOZE_TIME+" IS NULL AND "+ThreadRowMapper.CREATION_TIME+" >= :creationSince )";
		return fill(template.query(sql,source,new ThreadRowMapper()));
	}

	@Override
	public Set<ThreadRow> findBySpecs(ThreadRow threadRow, String follower) {
		String sql = SQL + " t ";
		MapSqlParameterSource source = new MapSqlParameterSource();
		if(follower!=null){
			sql = sql + " INNER JOIN "+ThreadRowFollowerMapper.DATABASE+"."+ThreadRowFollowerMapper.TABLE_NAME+" f " +
					" ON t."+ThreadRowMapper.ID+" = f."+ThreadRowFollowerMapper.THREAD_ID+" " +
					" WHERE f."+ThreadRowFollowerMapper.FOLLOWER+" = :follower ";
			source.addValue("follower", follower);
		}else{
			sql = sql + " WHERE 1=1 ";
		}
		if(threadRow!=null){
			if(threadRow.getId()!=null){
				sql = sql + " AND t."+ThreadRowMapper.ID+" = :threadId";
				source.addValue("threadId", threadRow.getId());
			}
			if(threadRow.getSnoozeTime()!=null){
				sql = sql + " AND t."+ThreadRowMapper.SNOOZE_TIME+" >= :snoozeTime";
				source.addValue("snoozeTime", threadRow.getSnoozeTime());
			}
			if(threadRow.getCreationTime()!=null){
				sql = sql + " AND t."+ThreadRowMapper.CREATION_TIME+" >= :creationTime";
				source.addValue("creationTime", threadRow.getCreationTime());
			}
			if(threadRow.getReplyTime()!=null){
				sql = sql + " AND t."+ThreadRowMapper.REPLY_TIME+" >= :replyTime";
				source.addValue("replyTime", threadRow.getReplyTime());
			}else{
				sql = sql + " AND t."+ThreadRowMapper.REPLY_TIME+" IS NULL ";
			}
			if(threadRow.getSubject()!=null){
				sql = sql + " AND t."+ThreadRowMapper.SUBJECT+" like :subject";
				source.addValue("subject", threadRow.getSubject());
			}
			if(threadRow.getPk()!=null && threadRow.getPk().getMessageId()!=null){
				sql = sql + " AND t."+ThreadRowMapper.MESSAGE_ID+" = :messageId";
				source.addValue("messageId", threadRow.getPk().getMessageId());
			}
			if(threadRow.getPk()!=null && threadRow.getPk().getSenderMail()!=null){
				sql = sql + " AND t."+ThreadRowMapper.SENDER_MAIL+" = :senderMail";
				source.addValue("senderMail", threadRow.getPk().getSenderMail());
			}
			if(threadRow.getPk()!=null && threadRow.getPk().getRecipientMail()!=null){
				sql = sql + " AND t."+ThreadRowMapper.RECIPIENT_MAIL+" = :recipientMail";
				source.addValue("recipientMail", threadRow.getPk().getRecipientMail());
			}
		}

		Map<ThreadRowPk, ThreadRow> rowResults = fill(template.query(sql, source,new ThreadRowMapper()));

		if(rowResults!=null && rowResults.size()>0){
			return new HashSet<ThreadRow>(rowResults.values());
		}
		return null;
	}
	
	private Map<ThreadRowPk, ThreadRow> fill(List<ThreadRow> rowResults){
		Map<ThreadRowPk, ThreadRow> threadRows = new HashMap<ThreadRowPk, ThreadRow>();
		if(rowResults!=null && rowResults.size()>0){
			for(ThreadRow threadRow : rowResults){
				List<ThreadRowFollower> followresResult = findFollowers(threadRow.getId());
				if(followresResult!=null && followresResult.size()>0){
					threadRow.setFollowers(new HashSet<ThreadRowFollower>());
					for(ThreadRowFollower follower : followresResult){
						follower.setThreadRow(threadRow);
						threadRow.getFollowers().add(follower);
					}
				}
				threadRows.put(threadRow.getPk(), threadRow);
			}
		}
		return threadRows;
	}
	
	private List<ThreadRowFollower> findFollowers(Long threadRowId){
		String sql = " SELECT `"+ThreadRowFollowerMapper.FOLLOWER+"`, `"+ThreadRowFollowerMapper.PARAMETERS+"` " +
					" FROM `"+ThreadRowFollowerMapper.DATABASE+"`.`"+ThreadRowFollowerMapper.TABLE_NAME+"`" +
					" WHERE `"+ThreadRowFollowerMapper.THREAD_ID+"` = :threadId ;";
		return template.query(sql, new MapSqlParameterSource("threadId",threadRowId), new ThreadRowFollowerMapper());
	}
	
}

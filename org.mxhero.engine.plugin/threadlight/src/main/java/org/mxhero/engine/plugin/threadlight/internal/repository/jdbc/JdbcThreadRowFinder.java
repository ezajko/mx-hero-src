package org.mxhero.engine.plugin.threadlight.internal.repository.jdbc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.mxhero.engine.plugin.threadlight.internal.pagination.common.PageResult;
import org.mxhero.engine.plugin.threadlight.internal.pagination.jdbc.BaseJdbcDao;
import org.mxhero.engine.plugin.threadlight.internal.pagination.jdbc.JdbcPageInfo;
import org.mxhero.engine.plugin.threadlight.internal.repository.ThreadRowFinder;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRowFollower;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRowPk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("jdbcFinder")
@Transactional(readOnly=true)
public class JdbcThreadRowFinder extends BaseJdbcDao<ThreadRow> implements ThreadRowFinder{

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
		
		String sql = SQL + " WHERE (("+ThreadRowMapper.SNOOZE_TIME+" IS NOT NULL AND "+ThreadRowMapper.SNOOZE_TIME+" >= :snoozeSince) " +
				" OR ("+ThreadRowMapper.SNOOZE_TIME+" IS NULL AND "+ThreadRowMapper.CREATION_TIME+" >= :creationSince )) " +
				" AND "+ThreadRowMapper.REPLY_TIME+" IS NULL";
		List<ThreadRow> rowResults = template.query(sql,source,new ThreadRowMapper());
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

	@Override
	public PageResult<ThreadRow> findBySpecs(ThreadRow threadRow, String follower, int pageNo, int pageSize) {
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
		
		JdbcPageInfo pi = new JdbcPageInfo();
		pi.setOrderByList(new ArrayList<String>());
		pi.getOrderByList().add("`"+ThreadRowMapper.MESSAGE_ID+"`");
		pi.getOrderByList().add("`"+ThreadRowMapper.SENDER_MAIL+"`");
		pi.getOrderByList().add("`"+ThreadRowMapper.RECIPIENT_MAIL+"`");
		pi.setPageNo(pageNo);
		pi.setPageSize(pageSize);
		pi.putRowMapper(new ThreadRowMapper());
		pi.putSql(sql);
		pi.putExampleModel(source.getValues());
		PageResult<ThreadRow> result = super.findByPage(pi);

		if(result!=null && result.getPageData()!=null){
			for(ThreadRow row : result.getPageData()){
				List<ThreadRowFollower> followresResult = findFollowers(row.getId());
				if(followresResult!=null && followresResult.size()>0){
					row.setFollowers(new HashSet<ThreadRowFollower>());
					for(ThreadRowFollower followerRecord : followresResult){
						followerRecord.setThreadRow(row);
						row.getFollowers().add(followerRecord);
					}
				}
			}
		}
		
		return null;
	}
	
	private List<ThreadRowFollower> findFollowers(Long threadRowId){
		String sql = " SELECT `"+ThreadRowFollowerMapper.FOLLOWER+"`, `"+ThreadRowFollowerMapper.PARAMETERS+"` " +
					" FROM `"+ThreadRowFollowerMapper.DATABASE+"`.`"+ThreadRowFollowerMapper.TABLE_NAME+"`" +
					" WHERE `"+ThreadRowFollowerMapper.THREAD_ID+"` = :threadId ;";
		return template.query(sql, new MapSqlParameterSource("threadId",threadRowId), new ThreadRowFollowerMapper());
	}
	
}

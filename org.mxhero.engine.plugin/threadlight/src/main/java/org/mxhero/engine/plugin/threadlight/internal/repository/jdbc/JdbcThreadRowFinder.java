package org.mxhero.engine.plugin.threadlight.internal.repository.jdbc;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value="mxhero",readOnly=true)
public class JdbcThreadRowFinder implements ThreadRowFinder{

	private NamedParameterJdbcTemplate template;
	private static final String SQL = " SELECT * FROM `"+ThreadRowMapper.DATABASE+"`.`"+ThreadRowMapper.TABLE_NAME+"` ";
	
	@Autowired
	public JdbcThreadRowFinder(@Qualifier("mxheroDataSource")DataSource ds) {
		this.template = new NamedParameterJdbcTemplate(ds);
	}

	@Override
	public Set<ThreadRow> findBySpecs(ThreadRow threadRow, String follower) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Map<ThreadRowPk, ThreadRow> findAll() {
		List<ThreadRow> rowResults = template.getJdbcOperations().query(SQL, new ThreadRowMapper());
		return fill(rowResults);
	}

	private Map<ThreadRowPk, ThreadRow> fill(List<ThreadRow> rowResults){
		Map<ThreadRowPk, ThreadRow> threadRows = null;
		if(rowResults!=null && rowResults.size()>0){
			threadRows = new HashMap<ThreadRowPk, ThreadRow>();
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
		String sql = " SELECT `"+ThreadRowFollowerMapper.FOLLOWER+"` " +
					" FROM `"+ThreadRowFollowerMapper.DATABASE+"`.`"+ThreadRowFollowerMapper.TABLE_NAME+"`" +
					" WHERE `"+ThreadRowFollowerMapper.THREAD_ID+"` = :threadId ;";
		return template.query(sql, new MapSqlParameterSource("threadId",threadRowId), new ThreadRowFollowerMapper());
	}
	
}

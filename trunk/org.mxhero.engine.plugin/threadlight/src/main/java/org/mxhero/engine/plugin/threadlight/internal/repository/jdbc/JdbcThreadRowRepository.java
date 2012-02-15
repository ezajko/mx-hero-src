package org.mxhero.engine.plugin.threadlight.internal.repository.jdbc;

import javax.sql.DataSource;

import org.mxhero.engine.plugin.threadlight.internal.repository.ThreadRowRepository;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRowPk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value="mxhero",readOnly=true)
public class JdbcThreadRowRepository implements ThreadRowRepository{

	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcThreadRowRepository(@Qualifier("mxheroDataSource")DataSource ds) {
		this.template = new NamedParameterJdbcTemplate(ds);
	}

	@Override
	public ThreadRow find(ThreadRowPk pk) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveThread(ThreadRow threadRow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addFollower(ThreadRowPk pk, String follower) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFollower(ThreadRowPk pk, String follower) {
		// TODO Auto-generated method stub
		
	}

}

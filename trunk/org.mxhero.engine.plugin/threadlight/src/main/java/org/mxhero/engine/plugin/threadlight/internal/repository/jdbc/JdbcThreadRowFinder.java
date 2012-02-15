package org.mxhero.engine.plugin.threadlight.internal.repository.jdbc;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.mxhero.engine.plugin.threadlight.internal.repository.ThreadRowFinder;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRowPk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value="mxhero",readOnly=true)
public class JdbcThreadRowFinder implements ThreadRowFinder{

	private NamedParameterJdbcTemplate template;
	
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
		// TODO Auto-generated method stub
		return null;
	}

}

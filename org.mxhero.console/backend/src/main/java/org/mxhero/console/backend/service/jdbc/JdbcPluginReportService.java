package org.mxhero.console.backend.service.jdbc;

import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.mxhero.console.backend.service.PluginReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("jdbcPluginReportService")
public class JdbcPluginReportService implements PluginReportService{

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public JdbcPluginReportService(@Qualifier("statisticsDataSource")DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcTemplate.setMaxRows(PluginReportService.MAX_RESULT);
    }
	
	@Override
	@Transactional(value="statistics",readOnly=true)
	public Collection getResult(String queryString, List params) {
	
		if(params!=null && params.size()>0){
			return this.jdbcTemplate.queryForList(queryString,params.toArray());
		}else{
			return this.jdbcTemplate.queryForList(queryString);
		}

	}

}

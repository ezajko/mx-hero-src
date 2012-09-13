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

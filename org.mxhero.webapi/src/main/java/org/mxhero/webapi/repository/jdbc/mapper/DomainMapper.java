package org.mxhero.webapi.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.mxhero.webapi.vo.DomainVO;
import org.springframework.jdbc.core.RowMapper;

public class DomainMapper implements RowMapper<DomainVO>{

	public static final String DATABASE = "mxhero";
	public static final String TABLE_NAME = "domain";
	
	public static final String DOMAIN = "domain";
	public static final String CREATION = "creation";
	public static final String SERVER = "server";
	public static final String UPDATED = "updated";
	
	public static final String ALIASES_TABLE_NAME = "domains_aliases";
	public static final String ALIASES_ALIAS = "alias";
	public static final String ALIASES_CREATED = "created";
	public static final String ALIASES_DOMAIN = "domain";
	
	@Override
	public DomainVO mapRow(ResultSet rs, int param) throws SQLException {
		DomainVO domain = new DomainVO();

		if(rs.getTimestamp(CREATION)!=null){
			Calendar creationDate = Calendar.getInstance();
			creationDate.setTimeInMillis(rs.getTimestamp(CREATION).getTime());
			domain.setCreationDate(creationDate);
		}
		domain.setDomain(rs.getString(DOMAIN));
		domain.setServer(rs.getString(SERVER));
		if(rs.getTimestamp(UPDATED)!=null){
			Calendar updatedDate = Calendar.getInstance();
			updatedDate.setTimeInMillis(rs.getTimestamp(UPDATED).getTime());
			domain.setUpdatedDate(updatedDate);
		}
		
		return domain;
	}

}

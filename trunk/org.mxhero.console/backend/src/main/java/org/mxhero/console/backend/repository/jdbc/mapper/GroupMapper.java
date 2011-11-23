package org.mxhero.console.backend.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.mxhero.console.backend.vo.GroupVO;
import org.springframework.jdbc.core.RowMapper;

public class GroupMapper implements RowMapper<GroupVO>{

	public static final String DATABASE="mxhero";
	public static final String TABLE_NAME="groups";
	
	public static final String DOMAIN_ID="domain_id";
	public static final String NAME="name";
	public static final String CREATED="created";
	public static final String DESCRIPTION="description";
	public static final String UPDATED="updated";
	
	@Override
	public GroupVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		GroupVO group = new GroupVO();
		if(rs.getTimestamp(CREATED)!=null){
			Calendar createdDate = Calendar.getInstance();
			createdDate.setTimeInMillis(rs.getTimestamp(CREATED).getTime());
			group.setCreatedDate(createdDate);
		}
		group.setDescription(rs.getString(DESCRIPTION));
		group.setDomain(rs.getString(DOMAIN_ID));
		group.setName(rs.getString(NAME));
		if(rs.getTimestamp(UPDATED)!=null){
			Calendar updatedDate = Calendar.getInstance();
			updatedDate.setTimeInMillis(rs.getTimestamp(UPDATED).getTime());
			group.setUpdatedDate(updatedDate);
		}
		return group;
	}
	
}

package org.mxhero.console.backend.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.mxhero.console.backend.vo.EmailAccountVO;
import org.springframework.jdbc.core.RowMapper;

public class EmailAccountMapper implements RowMapper<EmailAccountVO>{

	public static final String DATABASE="mxhero";
	public static final String TABLE_NAME="email_accounts";
	
	public static final String ACCOUNT = "account";
	public static final String DOMAIN_ID = "domain_id";
	public static final String CREATED = "created";
	public static final String DATA_SOURCE = "data_source";
	public static final String UPDATED = "updated";
	public static final String GROUP_NAME = "group_name";
	
	@Override
	public EmailAccountVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		EmailAccountVO email = new EmailAccountVO();
		email.setAccount(rs.getString(ACCOUNT));
		if(rs.getTimestamp(CREATED)!=null){
			Calendar created = Calendar.getInstance();
			created.setTimeInMillis(rs.getTimestamp(CREATED).getTime());
			email.setCreatedDate(created);
		}
		email.setDataSource(rs.getString(DATA_SOURCE));
		email.setDomain(rs.getString(DOMAIN_ID));
		email.setGroup(rs.getString(GROUP_NAME));
		if(rs.getTimestamp(UPDATED)!=null){
			Calendar updatedDate = Calendar.getInstance();
			updatedDate.setTimeInMillis(rs.getTimestamp(UPDATED).getTime());
			email.setUpdatedDate(updatedDate);
		}
		return email;
	}
	
}

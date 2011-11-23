package org.mxhero.console.backend.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mxhero.console.backend.vo.EmailAccountAliasVO;
import org.springframework.jdbc.core.RowMapper;

public class EmailAccountAliasMapper implements RowMapper<EmailAccountAliasVO>{

	public static final String DATABASE="mxhero";
	public static final String TABLE_NAME="account_aliases";
	
	public static final String ACCOUNT_ALIAS="account_alias";
	public static final String DOMAIN_ALIAS="domain_alias";
	public static final String CREATED="created";
	public static final String DATA_SOURCE="data_source";
	public static final String ACCOUNT="account";
	public static final String DOMAIN_ID="domain_id";
	
	@Override
	public EmailAccountAliasVO mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		EmailAccountAliasVO alias = new EmailAccountAliasVO();
		alias.setDataSource(rs.getString(DATA_SOURCE));
		alias.setDomain(rs.getString(DOMAIN_ALIAS));
		alias.setName(rs.getString(ACCOUNT_ALIAS));
		return alias;
	}

}

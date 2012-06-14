package org.mxhero.webapi.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mxhero.webapi.vo.AccountAliasVO;
import org.springframework.jdbc.core.RowMapper;

public class AccountAliasMapper implements RowMapper<AccountAliasVO>{

	public static final String DATABASE="mxhero";
	public static final String TABLE_NAME="account_aliases";
	
	public static final String ACCOUNT_ALIAS="account_alias";
	public static final String DOMAIN_ALIAS="domain_alias";
	public static final String CREATED="created";
	public static final String DATA_SOURCE="data_source";
	public static final String ACCOUNT="account";
	public static final String DOMAIN_ID="domain_id";
	
	@Override
	public AccountAliasVO mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		AccountAliasVO alias = new AccountAliasVO();
		alias.setDataSource(rs.getString(DATA_SOURCE));
		alias.setDomain(rs.getString(DOMAIN_ALIAS));
		alias.setName(rs.getString(ACCOUNT_ALIAS));
		return alias;
	}

}

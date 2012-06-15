package org.mxhero.webapi.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mxhero.webapi.vo.AccountPropertyVO;
import org.springframework.jdbc.core.RowMapper;

public class AccountPropertyMapper implements RowMapper<AccountPropertyVO>{

	public static final String DATABASE="mxhero";
	public static final String TABLE_NAME="email_accounts_properties";
	
	public static final String ACCOUNT = "account";
	public static final String DOMAIN = "domain_id";
	public static final String NAME = "property_name";
	public static final String VALUE = "property_value";
	@Override
	public AccountPropertyVO mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		AccountPropertyVO vo = new AccountPropertyVO();
		vo.setName(rs.getString(NAME));
		vo.setValue(rs.getString(VALUE));
		return vo;
	}
}

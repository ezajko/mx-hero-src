package org.mxhero.console.backend.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.mxhero.console.backend.vo.DomainAdLdapVO;
import org.springframework.jdbc.core.RowMapper;

public class DomainAdLdapMapper implements RowMapper<DomainAdLdapVO> {

	public static final String DATABASE = "mxhero";
	public static final String TABLE_NAME = "domain_adldap";
	
	public static final String DOMAIN = "domain";
	public static final String ADDRESS = "address";
	public static final String BASE ="base";
	public static final String DIRECTORY_TYPE = "directory_type";
	public static final String LAST_ERROR = "last_error";
	public static final String FILTER = "filter";
	public static final String LAST_UPDATE = "last_update";
	public static final String NEXT_UPDATE = "next_update";
	public static final String OVERRIDE_FLAG = "override_flag";
	public static final String PASSWORD = "password";
	public static final String PORT = "port";
	public static final String SSL_FLAG = "ssl_flag";
	public static final String USER = "user";
	public static final String DN_AUTHENTICATE = "dn_authenticate";
	public static final String ACCOUNT_PROPERTIES = "account_properties";
	
	@Override
	public DomainAdLdapVO mapRow(ResultSet rs, int param) throws SQLException {
		Calendar lastUpdate = null;
		Calendar nextUpdate = null;
		DomainAdLdapVO adladap = new DomainAdLdapVO();
		adladap.setAddres(rs.getString(ADDRESS));
		adladap.setBase(rs.getString(BASE));
		adladap.setDirectoryType(rs.getString(DIRECTORY_TYPE));
		adladap.setDnAuthenticate(rs.getString(DN_AUTHENTICATE));
		adladap.setDomainId(rs.getString(DOMAIN));
		adladap.setError(rs.getString(LAST_ERROR));
		adladap.setFilter(rs.getString(FILTER));
		if(rs.getTimestamp(LAST_UPDATE)!=null){
			lastUpdate = Calendar.getInstance();
			lastUpdate.setTimeInMillis(rs.getTimestamp(LAST_UPDATE).getTime());
		}
		adladap.setLastUpdate(lastUpdate);
		if(rs.getTimestamp(NEXT_UPDATE)!=null){
			nextUpdate = Calendar.getInstance();
			nextUpdate.setTimeInMillis(rs.getTimestamp(NEXT_UPDATE).getTime());
		}
		adladap.setNextUpdate(nextUpdate);
		adladap.setOverrideFlag(rs.getBoolean(OVERRIDE_FLAG));
		adladap.setPassword(rs.getString(PASSWORD));
		adladap.setPort(rs.getString(PORT));
		adladap.setSslFlag(rs.getBoolean(SSL_FLAG));
		adladap.setUser(rs.getString(USER));
		adladap.setAccountProperties(rs.getString(ACCOUNT_PROPERTIES));
		return adladap;
	}

}

package org.mxhero.webapi.repository.jdbc;

import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.mxhero.webapi.repository.LdapRepository;
import org.mxhero.webapi.repository.jdbc.mapper.EmailAccountAliasMapper;
import org.mxhero.webapi.repository.jdbc.mapper.EmailAccountMapper;
import org.mxhero.webapi.repository.jdbc.mapper.LdapMapper;
import org.mxhero.webapi.repository.jdbc.mapper.LdapPropertyMapper;
import org.mxhero.webapi.vo.LdapPropertyVO;
import org.mxhero.webapi.vo.LdapVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value="mxhero",readOnly=true)
public class JdbcLdapRepository implements LdapRepository{

	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcLdapRepository(@Qualifier("mxheroDataSource")DataSource ds) {
		this.template =  new NamedParameterJdbcTemplate(ds);
	}

	@Override
	public LdapVO findByDomainId(String domainId) {
		String sql = "SELECT `"+LdapMapper.DOMAIN+"`,`"+LdapMapper.ADDRESS+"`,"+
				" `"+LdapMapper.BASE+"`,`"+LdapMapper.DIRECTORY_TYPE+"`,"+
				" `"+LdapMapper.LAST_ERROR+"`,`"+LdapMapper.FILTER+"`,"+
				" `"+LdapMapper.LAST_UPDATE+"`,`"+LdapMapper.NEXT_UPDATE+"`,"+
				" `"+LdapMapper.OVERRIDE_FLAG+"`,`"+LdapMapper.PASSWORD+"`,"+
				" `"+LdapMapper.PORT+"`,`"+LdapMapper.SSL_FLAG+"`,"+
				" `"+LdapMapper.USER+"`,`"+LdapMapper.DN_AUTHENTICATE+"`" +
				" FROM `"+LdapMapper.DATABASE+"`.`"+LdapMapper.TABLE_NAME+"`" +
				" WHERE `"+LdapMapper.DOMAIN+"` = :domainId";
		List<LdapVO> adldaps = template.query(sql, new MapSqlParameterSource("domainId",domainId), new LdapMapper());
		if(adldaps!=null && adldaps.size()>0){
			LdapVO adldap = adldaps.get(0);
			String sqlProperties = "SELECT `"+LdapPropertyMapper.PROPERTY_NAME+"`,`"+LdapPropertyMapper.PROPERTY_KEY+"`"+
					" FROM `"+LdapPropertyMapper.DATABASE+"`.`"+LdapPropertyMapper.TABLE_NAME+"`" +
					" WHERE `"+LdapPropertyMapper.DOMAIN+"` = :domainId";
			adldap.setProperties(template.query(sqlProperties, new MapSqlParameterSource("domainId",domainId), new LdapPropertyMapper()));
			return adldap;
		}
		return null;
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public LdapVO update(LdapVO domainAdLdapVO) {
		String sql = "UPDATE `"+LdapMapper.DATABASE+"`.`"+LdapMapper.TABLE_NAME+"`" +
				" SET `"+LdapMapper.ADDRESS+"` = :address,"+
				" `"+LdapMapper.BASE+"` = :base," +
				" `"+LdapMapper.DIRECTORY_TYPE+"` = :directoryType,"+
				" `"+LdapMapper.FILTER+"` = :filter,"+
				" `"+LdapMapper.NEXT_UPDATE+"` = :nextUpdate,"+
				" `"+LdapMapper.OVERRIDE_FLAG+"` = :overrideFlag," +
				" `"+LdapMapper.PASSWORD+"` = :password,"+
				" `"+LdapMapper.PORT+"` = :port," +
				" `"+LdapMapper.SSL_FLAG+"` = :sslFlag,"+
				" `"+LdapMapper.USER+"` = :user," +
				" `"+LdapMapper.DN_AUTHENTICATE+"` = :dnAuthenticate" +
				" WHERE `"+LdapMapper.DOMAIN+"` = :domainId";
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("address", domainAdLdapVO.getAddres());
		source.addValue("base", domainAdLdapVO.getBase());
		source.addValue("directoryType", domainAdLdapVO.getDirectoryType());
		source.addValue("filter", domainAdLdapVO.getFilter());
		source.addValue("nextUpdate", domainAdLdapVO.getNextUpdate().getTime());
		source.addValue("overrideFlag", domainAdLdapVO.getOverrideFlag());
		source.addValue("password", domainAdLdapVO.getPassword());
		source.addValue("port", domainAdLdapVO.getPort());
		source.addValue("sslFlag", domainAdLdapVO.getSslFlag());
		source.addValue("user", domainAdLdapVO.getUser());
		source.addValue("dnAuthenticate", domainAdLdapVO.getDnAuthenticate());
		source.addValue("domainId", domainAdLdapVO.getDomainId());
		template.update(sql, source);
		
		template.update("DELETE FROM `"+LdapPropertyMapper.DATABASE+"`.`"+LdapPropertyMapper.TABLE_NAME+"` WHERE `"+LdapPropertyMapper.DOMAIN+"` = :domainId"
				, new MapSqlParameterSource("domainId",domainAdLdapVO.getDomainId()));
		if(domainAdLdapVO.getProperties()!=null){
			for(LdapPropertyVO property : domainAdLdapVO.getProperties()){
				MapSqlParameterSource propertySource = new MapSqlParameterSource();
				propertySource.addValue("name", property.getName());
				propertySource.addValue("key", property.getKey());
				propertySource.addValue("domain", domainAdLdapVO.getDomainId());
				template.update("INSERT INTO `"+LdapPropertyMapper.DATABASE+"`.`"+LdapPropertyMapper.TABLE_NAME+"` " +
						" (`"+LdapPropertyMapper.PROPERTY_NAME+"`,`"+LdapPropertyMapper.PROPERTY_KEY+"`,`"+LdapPropertyMapper.DOMAIN+"`) " +
						" VALUES (:name,:key,:domain) ;",propertySource);
			}
		}
		
		return findByDomainId(domainAdLdapVO.getDomainId());
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public LdapVO insert(LdapVO domainAdLdapVO) {
		String sql = "INSERT INTO `"+LdapMapper.DATABASE+"`.`"+LdapMapper.TABLE_NAME+"`" +
				" (`"+LdapMapper.DOMAIN+"`,`"+LdapMapper.ADDRESS+"`,"+
				" `"+LdapMapper.BASE+"`,`"+LdapMapper.DIRECTORY_TYPE+"`,"+
				" `"+LdapMapper.FILTER+"`,"+
				" `"+LdapMapper.NEXT_UPDATE+"`,"+
				" `"+LdapMapper.OVERRIDE_FLAG+"`,`"+LdapMapper.PASSWORD+"`,"+
				" `"+LdapMapper.PORT+"`,`"+LdapMapper.SSL_FLAG+"`,"+
				" `"+LdapMapper.USER+"`,"+" `"+LdapMapper.DN_AUTHENTICATE+"`)" +
				" VALUES (:domainId, :address, :base, :directoryType, :filter, :nextUpdate," +
				" :overrideFlag, :password, :port, :sslFlag, :user, :dnAuthenticate) ;";
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("domainId", domainAdLdapVO.getDomainId());
		source.addValue("address", domainAdLdapVO.getAddres());
		source.addValue("base", domainAdLdapVO.getBase());
		source.addValue("directoryType", domainAdLdapVO.getDirectoryType());
		source.addValue("filter", domainAdLdapVO.getFilter());
		source.addValue("nextUpdate", Calendar.getInstance().getTime());
		source.addValue("overrideFlag", domainAdLdapVO.getOverrideFlag());
		source.addValue("password", domainAdLdapVO.getPassword());
		source.addValue("port", domainAdLdapVO.getPort());
		source.addValue("sslFlag", domainAdLdapVO.getSslFlag());
		source.addValue("user", domainAdLdapVO.getUser());
		source.addValue("dnAuthenticate", domainAdLdapVO.getDnAuthenticate());
		template.update(sql, source);
		template.update("DELETE FROM `"+LdapPropertyMapper.DATABASE+"`.`"+LdapPropertyMapper.TABLE_NAME+"` WHERE `"+LdapPropertyMapper.DOMAIN+"` = :domainId"
				, new MapSqlParameterSource("domainId",domainAdLdapVO.getDomainId()));
		if(domainAdLdapVO.getProperties()!=null){
			for(LdapPropertyVO property : domainAdLdapVO.getProperties()){
				MapSqlParameterSource propertySource = new MapSqlParameterSource();
				propertySource.addValue("name", property.getName());
				propertySource.addValue("key", property.getKey());
				propertySource.addValue("domain", domainAdLdapVO.getDomainId());
				template.update("INSERT INTO `"+LdapPropertyMapper.DATABASE+"`.`"+LdapPropertyMapper.TABLE_NAME+"` " +
						" (`"+LdapPropertyMapper.PROPERTY_NAME+"`,`"+LdapPropertyMapper.PROPERTY_KEY+"`,`"+LdapPropertyMapper.DOMAIN+"`) " +
						" VALUES (:name,:key,:domain) ;",propertySource);
			}
		}		
		return findByDomainId(domainAdLdapVO.getDomainId());
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void deleteByDomainId(String domainId) {
		String sql = "DELETE FROM `"+LdapMapper.DATABASE+"`.`"+LdapMapper.TABLE_NAME+"`" +
				" WHERE `"+LdapMapper.DOMAIN+"` = :domainId";
		template.update(sql, new MapSqlParameterSource("domainId", domainId));
		String emailAliasesSql = "UPDATE `"+EmailAccountMapper.DATABASE+"`.`"+EmailAccountMapper.TABLE_NAME+"`" +
				" SET `"+EmailAccountMapper.DATA_SOURCE+"` = :dataSource " +
				" WHERE `"+EmailAccountMapper.DOMAIN_ID+"` = :domainId; ";
		MapSqlParameterSource emailParams = new MapSqlParameterSource("domainId", domainId);
		emailParams.addValue("dataSource", LdapVO.MANUAL);
		template.update(emailAliasesSql, emailParams);
		String emailSql = "UPDATE `"+EmailAccountAliasMapper.DATABASE+"`.`"+EmailAccountAliasMapper.TABLE_NAME+"`" +
				" SET `"+EmailAccountAliasMapper.DATA_SOURCE+"` = :dataSource " +
				" WHERE `"+EmailAccountAliasMapper.DOMAIN_ID+"` = :domainId; ";
		template.update(emailSql, emailParams);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void refresh(String domainId) {
		String sql = "UPDATE `"+LdapMapper.DATABASE+"`.`"+LdapMapper.TABLE_NAME+"`" +
				" SET  `"+LdapMapper.NEXT_UPDATE+"` = :nextUpdate,"+
				" `"+LdapMapper.LAST_ERROR+"` = :lastError"+
				" WHERE `"+LdapMapper.DOMAIN+"` = :domainId";
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("nextUpdate", Calendar.getInstance().getTime());
		source.addValue("lastError", null);
		source.addValue("domainId", domainId);
		template.update(sql, source);
	}


}

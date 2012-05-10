package org.mxhero.console.backend.repository.jdbc;

import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.mxhero.console.backend.repository.DomainAdLdapRepository;
import org.mxhero.console.backend.repository.jdbc.mapper.DomainAdLdapMapper;
import org.mxhero.console.backend.repository.jdbc.mapper.DomainAdLdapPropertyMapper;
import org.mxhero.console.backend.repository.jdbc.mapper.EmailAccountAliasMapper;
import org.mxhero.console.backend.repository.jdbc.mapper.EmailAccountMapper;
import org.mxhero.console.backend.vo.DomainAdLdapPropertyVO;
import org.mxhero.console.backend.vo.DomainAdLdapVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value="mxhero",readOnly=true)
public class JdbcDomainAdLdapRepository implements DomainAdLdapRepository{

	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcDomainAdLdapRepository(@Qualifier("mxheroDataSource")DataSource ds) {
		this.template =  new NamedParameterJdbcTemplate(ds);
	}

	@Override
	public DomainAdLdapVO findByDomainId(String domainId) {
		String sql = "SELECT `"+DomainAdLdapMapper.DOMAIN+"`,`"+DomainAdLdapMapper.ADDRESS+"`,"+
				" `"+DomainAdLdapMapper.BASE+"`,`"+DomainAdLdapMapper.DIRECTORY_TYPE+"`,"+
				" `"+DomainAdLdapMapper.LAST_ERROR+"`,`"+DomainAdLdapMapper.FILTER+"`,"+
				" `"+DomainAdLdapMapper.LAST_UPDATE+"`,`"+DomainAdLdapMapper.NEXT_UPDATE+"`,"+
				" `"+DomainAdLdapMapper.OVERRIDE_FLAG+"`,`"+DomainAdLdapMapper.PASSWORD+"`,"+
				" `"+DomainAdLdapMapper.PORT+"`,`"+DomainAdLdapMapper.SSL_FLAG+"`,"+
				" `"+DomainAdLdapMapper.USER+"`,`"+DomainAdLdapMapper.DN_AUTHENTICATE+"`" +
				" FROM `"+DomainAdLdapMapper.DATABASE+"`.`"+DomainAdLdapMapper.TABLE_NAME+"`" +
				" WHERE `"+DomainAdLdapMapper.DOMAIN+"` = :domainId";
		List<DomainAdLdapVO> adldaps = template.query(sql, new MapSqlParameterSource("domainId",domainId), new DomainAdLdapMapper());
		if(adldaps!=null && adldaps.size()>0){
			DomainAdLdapVO adldap = adldaps.get(0);
			String sqlProperties = "SELECT `"+DomainAdLdapPropertyMapper.PROPERTY_NAME+"`,`"+DomainAdLdapPropertyMapper.PROPERTY_KEY+"`"+
					" FROM `"+DomainAdLdapPropertyMapper.DATABASE+"`.`"+DomainAdLdapPropertyMapper.TABLE_NAME+"`" +
					" WHERE `"+DomainAdLdapPropertyMapper.DOMAIN+"` = :domainId";
			adldap.setAccountProperties(template.query(sqlProperties, new MapSqlParameterSource("domainId",domainId), new DomainAdLdapPropertyMapper()));
			return adldap;
		}
		return null;
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public DomainAdLdapVO update(DomainAdLdapVO domainAdLdapVO) {
		String sql = "UPDATE `"+DomainAdLdapMapper.DATABASE+"`.`"+DomainAdLdapMapper.TABLE_NAME+"`" +
				" SET `"+DomainAdLdapMapper.ADDRESS+"` = :address,"+
				" `"+DomainAdLdapMapper.BASE+"` = :base," +
				" `"+DomainAdLdapMapper.DIRECTORY_TYPE+"` = :directoryType,"+
				" `"+DomainAdLdapMapper.FILTER+"` = :filter,"+
				" `"+DomainAdLdapMapper.NEXT_UPDATE+"` = :nextUpdate,"+
				" `"+DomainAdLdapMapper.OVERRIDE_FLAG+"` = :overrideFlag," +
				" `"+DomainAdLdapMapper.PASSWORD+"` = :password,"+
				" `"+DomainAdLdapMapper.PORT+"` = :port," +
				" `"+DomainAdLdapMapper.SSL_FLAG+"` = :sslFlag,"+
				" `"+DomainAdLdapMapper.USER+"` = :user," +
				" `"+DomainAdLdapMapper.DN_AUTHENTICATE+"` = :dnAuthenticate" +
				" WHERE `"+DomainAdLdapMapper.DOMAIN+"` = :domainId";
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
		
		template.update("DELETE FROM `"+DomainAdLdapPropertyMapper.DATABASE+"`.`"+DomainAdLdapPropertyMapper.TABLE_NAME+"` WHERE `"+DomainAdLdapPropertyMapper.DOMAIN+"` = :domainId"
				, new MapSqlParameterSource("domainId",domainAdLdapVO.getDomainId()));
		if(domainAdLdapVO.getAccountProperties()!=null){
			for(DomainAdLdapPropertyVO property : domainAdLdapVO.getAccountProperties()){
				MapSqlParameterSource propertySource = new MapSqlParameterSource();
				propertySource.addValue("name", property.getName());
				propertySource.addValue("key", property.getKey());
				propertySource.addValue("domain", domainAdLdapVO.getDomainId());
				template.update("INSERT INTO `"+DomainAdLdapPropertyMapper.DATABASE+"`.`"+DomainAdLdapPropertyMapper.TABLE_NAME+"` " +
						" (`"+DomainAdLdapPropertyMapper.PROPERTY_NAME+"`,`"+DomainAdLdapPropertyMapper.PROPERTY_KEY+"`,`"+DomainAdLdapPropertyMapper.DOMAIN+"`) " +
						" VALUES (:name,:key,:domain) ;",propertySource);
			}
		}
		
		return findByDomainId(domainAdLdapVO.getDomainId());
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public DomainAdLdapVO insert(DomainAdLdapVO domainAdLdapVO) {
		String sql = "INSERT INTO `"+DomainAdLdapMapper.DATABASE+"`.`"+DomainAdLdapMapper.TABLE_NAME+"`" +
				" (`"+DomainAdLdapMapper.DOMAIN+"`,`"+DomainAdLdapMapper.ADDRESS+"`,"+
				" `"+DomainAdLdapMapper.BASE+"`,`"+DomainAdLdapMapper.DIRECTORY_TYPE+"`,"+
				" `"+DomainAdLdapMapper.FILTER+"`,"+
				" `"+DomainAdLdapMapper.NEXT_UPDATE+"`,"+
				" `"+DomainAdLdapMapper.OVERRIDE_FLAG+"`,`"+DomainAdLdapMapper.PASSWORD+"`,"+
				" `"+DomainAdLdapMapper.PORT+"`,`"+DomainAdLdapMapper.SSL_FLAG+"`,"+
				" `"+DomainAdLdapMapper.USER+"`,"+" `"+DomainAdLdapMapper.DN_AUTHENTICATE+"`)" +
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
		template.update("DELETE FROM `"+DomainAdLdapPropertyMapper.DATABASE+"`.`"+DomainAdLdapPropertyMapper.TABLE_NAME+"` WHERE `"+DomainAdLdapPropertyMapper.DOMAIN+"` = :domainId"
				, new MapSqlParameterSource("domainId",domainAdLdapVO.getDomainId()));
		if(domainAdLdapVO.getAccountProperties()!=null){
			for(DomainAdLdapPropertyVO property : domainAdLdapVO.getAccountProperties()){
				MapSqlParameterSource propertySource = new MapSqlParameterSource();
				propertySource.addValue("name", property.getName());
				propertySource.addValue("key", property.getKey());
				propertySource.addValue("domain", domainAdLdapVO.getDomainId());
				template.update("INSERT INTO `"+DomainAdLdapPropertyMapper.DATABASE+"`.`"+DomainAdLdapPropertyMapper.TABLE_NAME+"` " +
						" (`"+DomainAdLdapPropertyMapper.PROPERTY_NAME+"`,`"+DomainAdLdapPropertyMapper.PROPERTY_KEY+"`,`"+DomainAdLdapPropertyMapper.DOMAIN+"`) " +
						" VALUES (:name,:key,:domain) ;",propertySource);
			}
		}		
		return findByDomainId(domainAdLdapVO.getDomainId());
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void deleteByDomainId(String domainId) {
		String sql = "DELETE FROM `"+DomainAdLdapMapper.DATABASE+"`.`"+DomainAdLdapMapper.TABLE_NAME+"`" +
				" WHERE `"+DomainAdLdapMapper.DOMAIN+"` = :domainId";
		template.update(sql, new MapSqlParameterSource("domainId", domainId));
		String emailAliasesSql = "UPDATE `"+EmailAccountMapper.DATABASE+"`.`"+EmailAccountMapper.TABLE_NAME+"`" +
				" SET `"+EmailAccountMapper.DATA_SOURCE+"` = :dataSource " +
				" WHERE `"+EmailAccountMapper.DOMAIN_ID+"` = :domainId; ";
		MapSqlParameterSource emailParams = new MapSqlParameterSource("domainId", domainId);
		emailParams.addValue("dataSource", DomainAdLdapVO.MANUAL);
		template.update(emailAliasesSql, emailParams);
		String emailSql = "UPDATE `"+EmailAccountAliasMapper.DATABASE+"`.`"+EmailAccountAliasMapper.TABLE_NAME+"`" +
				" SET `"+EmailAccountAliasMapper.DATA_SOURCE+"` = :dataSource " +
				" WHERE `"+EmailAccountAliasMapper.DOMAIN_ID+"` = :domainId; ";
		template.update(emailSql, emailParams);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void refresh(String domainId) {
		String sql = "UPDATE `"+DomainAdLdapMapper.DATABASE+"`.`"+DomainAdLdapMapper.TABLE_NAME+"`" +
				" SET  `"+DomainAdLdapMapper.NEXT_UPDATE+"` = :nextUpdate,"+
				" `"+DomainAdLdapMapper.LAST_ERROR+"` = :lastError"+
				" WHERE `"+DomainAdLdapMapper.DOMAIN+"` = :domainId";
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("nextUpdate", Calendar.getInstance().getTime());
		source.addValue("lastError", null);
		source.addValue("domainId", domainId);
		template.update(sql, source);
	}


}

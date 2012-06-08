package org.mxhero.webapi.repository.jdbc;

import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.mxhero.webapi.repository.RuleRepository;
import org.mxhero.webapi.repository.jdbc.mapper.FeatureMapper;
import org.mxhero.webapi.repository.jdbc.mapper.RuleDirectionMapper;
import org.mxhero.webapi.repository.jdbc.mapper.RuleMapper;
import org.mxhero.webapi.repository.jdbc.mapper.RulePropertyMapper;
import org.mxhero.webapi.vo.RuleDirectionVO;
import org.mxhero.webapi.vo.RulePropertyVO;
import org.mxhero.webapi.vo.RuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value="mxhero",readOnly=false)
public class JdbcRuleRepository implements RuleRepository{

	private static final String SELECT = "SELECT `"+RuleMapper.ADMIN_ORDER+"`," +
			" `"+RuleMapper.CREATED+"`, `"+RuleMapper.ENABLED+"`," +
			" `"+RuleMapper.FROM_DIRECTION_ID+"`, `"+RuleMapper.ID+"`," +
			" `"+RuleMapper.LABEL+"`, `"+RuleMapper.TO_DIRECTION_ID+"`," +
			" `"+RuleMapper.TWO_WAYS+"`,`"+RuleMapper.UPDATED+"`," +
			" `"+RuleMapper.DOMAIN_ID+"`, `"+FeatureMapper.COMPONENT+"`" +
			" FROM `"+RuleMapper.DATABASE+"`.`"+RuleMapper.TABLE_NAME+"`" +
			" INNER JOIN `"+FeatureMapper.DATABASE+"`.`"+FeatureMapper.TABLE_NAME+"` "
					+" ON `"+FeatureMapper.TABLE_NAME+"`.`"+FeatureMapper.ID+"` = `"+RuleMapper.TABLE_NAME+"`.`"+RuleMapper.FEATURE_ID+"` ";
	
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcRuleRepository(@Qualifier("mxheroDataSource")DataSource ds) {
		this.template =  new NamedParameterJdbcTemplate(ds);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void delete(Long ruleId) {
		String propertiesSql = "DELETE FROM `"+RulePropertyMapper.DATABASE+"`.`"+RulePropertyMapper.TABLE_NAME+"`" +
				" WHERE `"+RulePropertyMapper.RULE_ID+"` = :ruleId ;";
		template.update(propertiesSql, new MapSqlParameterSource("ruleId",ruleId));
		
		String directionSql = "DELETE FROM `"+RuleDirectionMapper.DATABASE+"`.`"+RuleDirectionMapper.TABLE_NAME+"`" +
				" WHERE `"+RuleDirectionMapper.RULE_ID+"` = :ruleId ;";
		template.update(directionSql, new MapSqlParameterSource("ruleId",ruleId));	
		
		String ruleSql = "DELETE FROM `"+RuleMapper.DATABASE+"`.`"+RuleMapper.TABLE_NAME+"`" +
				" WHERE `"+RuleMapper.ID+"` = :ruleId ;";
		template.update(ruleSql, new MapSqlParameterSource("ruleId",ruleId));			
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void deleteByDomain(String domainId) {
		String idsSql = "SELECT DISTINCT `"+RuleDirectionMapper.RULE_ID+"` " +
				" FROM `"+RuleDirectionMapper.DATABASE+"`.`"+RuleDirectionMapper.TABLE_NAME+"` " +
				" WHERE `"+RuleDirectionMapper.DOMAIN+"` = :domainId ;";
		List<Long> ids = template.queryForList(idsSql, new MapSqlParameterSource("domainId",domainId), Long.class);
		if(ids!=null && ids.size()>0){
			for(Long id : ids){
				delete(id);
			}
		}
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void deleteByAccount(String domainId, String accountId) {
		String idsSql = "SELECT DISTINCT `"+RuleDirectionMapper.RULE_ID+"` " +
				" FROM `"+RuleDirectionMapper.DATABASE+"`.`"+RuleDirectionMapper.TABLE_NAME+"` " +
				" WHERE `"+RuleDirectionMapper.DOMAIN+"` = :domainId " +
				" AND `"+RuleDirectionMapper.ACCOUNT+"` = :accountId ;";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",domainId);
		source.addValue("accountId", accountId);
		List<Long> ids = template.queryForList(idsSql, source, Long.class);
		if(ids!=null && ids.size()>0){
			for(Long id : ids){
				delete(id);
			}
		}
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void deleteByGroup(String domainId, String group) {
		String idsSql = "SELECT DISTINCT `"+RuleDirectionMapper.RULE_ID+"` " +
				" FROM `"+RuleDirectionMapper.DATABASE+"`.`"+RuleDirectionMapper.TABLE_NAME+"` " +
				" WHERE `"+RuleDirectionMapper.DOMAIN+"` = :domainId " +
				" AND `"+RuleDirectionMapper.GROUP_NAME+"` = :group ;";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",domainId);
		source.addValue("group", group);
		List<Long> ids = template.queryForList(idsSql, source, Long.class);
		if(ids!=null && ids.size()>0){
			for(Long id : ids){
				delete(id);
			}
		}
	}

	@Override
	public boolean checkFromTo(String domainId, String component,
			String fromFreeValue, String toFreeValue) {
		String sql = "SELECT SUM(1) FROM `"+RuleMapper.DATABASE+"`.`"+RuleMapper.TABLE_NAME+"` rules" +
				" INNER JOIN `"+RuleDirectionMapper.DATABASE+"`.`"+RuleDirectionMapper.TABLE_NAME+"` from_directions" +
				" ON from_directions.`"+RuleDirectionMapper.ID+"` = rules.`"+RuleMapper.FROM_DIRECTION_ID+"`" +
				" INNER JOIN `"+RuleDirectionMapper.DATABASE+"`.`"+RuleDirectionMapper.TABLE_NAME+"` to_directions" +
				" ON to_directions.`"+RuleDirectionMapper.ID+"` = rules.`"+RuleMapper.TO_DIRECTION_ID+"`" +
				" INNER JOIN `"+FeatureMapper.DATABASE+"`.`"+FeatureMapper.TABLE_NAME+"` features" +
				" ON features.`"+FeatureMapper.ID+"` = rules.`"+RuleMapper.FEATURE_ID+"` " +
				" WHERE to_directions.`"+RuleDirectionMapper.FREE_VALUE+"` = :toFreeValue" +
				" AND from_directions.`"+RuleDirectionMapper.FREE_VALUE+"` = :fromFreeValue" +
				" AND features.`"+FeatureMapper.COMPONENT+"` = :component"; 
		
		MapSqlParameterSource source = new MapSqlParameterSource("component",component);
		source.addValue("toFreeValue", toFreeValue);
		source.addValue("fromFreeValue", fromFreeValue);

		if(domainId!=null){
			sql = sql + " AND rules.`"+RuleMapper.DOMAIN_ID+"` = :domainId ;";
			source.addValue("domainId", domainId);
		}else{
			sql = sql + " AND rules.`"+RuleMapper.DOMAIN_ID+"` IS NULL ;";
		}
		return template.queryForInt(sql, source) > 0;
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public Long insert(RuleVO rule) {
		String ruleInsertSql = "INSERT INTO `"+RuleMapper.DATABASE+"`.`"+RuleMapper.TABLE_NAME+"`" +
				" (`"+RuleMapper.ADMIN_ORDER+"`,`"+RuleMapper.CREATED+"`,`"+RuleMapper.DOMAIN_ID+"`," +
				" `"+RuleMapper.ENABLED+"`,`"+RuleMapper.FEATURE_ID+"`,`"+RuleMapper.LABEL+"`," +
				" `"+RuleMapper.TWO_WAYS+"`,`"+RuleMapper.UPDATED+"`)" +
				" VALUES(:adminOrder,:created,:domainId,:enabled,:featureId,:label,:twoWays,:updated) ;";
		KeyHolder ruleKey = new GeneratedKeyHolder();
		MapSqlParameterSource ruleSource = new MapSqlParameterSource("adminOrder",rule.getAdminOrder());
		ruleSource.addValue("created", Calendar.getInstance().getTime());
		ruleSource.addValue("domainId", rule.getDomain());
		ruleSource.addValue("enabled", true);
		ruleSource.addValue("featureId", template.queryForLong(" SELECT `"+FeatureMapper.COMPONENT+"` FROM `"+FeatureMapper.TABLE_NAME+"` WHERE `"+FeatureMapper.COMPONENT+"` = :component", new MapSqlParameterSource("component",rule.getComponent())));
		ruleSource.addValue("label", rule.getName());
		ruleSource.addValue("twoWays", rule.getTwoWays());
		ruleSource.addValue("updated", Calendar.getInstance().getTime());
		template.update(ruleInsertSql, ruleSource, ruleKey);
		Integer fromDirectionId = insertDirection(rule.getFromDirection(),ruleKey.getKey().intValue());
		Integer toDirectionId = insertDirection(rule.getToDirection(),ruleKey.getKey().intValue());
		String updateDirectionsSql = "UPDATE `"+RuleMapper.DATABASE+"`.`"+RuleMapper.TABLE_NAME+"`" +
				" SET `"+RuleMapper.FROM_DIRECTION_ID+"` = :fromDirectionId ," +
				" `"+RuleMapper.TO_DIRECTION_ID+"` = :toDirectionId" +
				" WHERE `"+RuleMapper.ID+"` = :ruleId";
		MapSqlParameterSource ruleDirectionsSource = new MapSqlParameterSource("fromDirectionId",fromDirectionId);
		ruleDirectionsSource.addValue("toDirectionId",toDirectionId);
		ruleDirectionsSource.addValue("ruleId", ruleKey.getKey().intValue());
		template.update(updateDirectionsSql, ruleDirectionsSource);
		if(rule.getProperties()!=null && rule.getProperties().size()>0){
			for(RulePropertyVO property : rule.getProperties()){
				insertProperty(property,ruleKey.getKey().longValue());
			}
		}
		return ruleKey.getKey().longValue();
	}

	private Integer insertDirection(RuleDirectionVO direction, Integer ruleId){
		KeyHolder directionKey = new GeneratedKeyHolder();
		String sql = "INSERT INTO `"+RuleDirectionMapper.DATABASE+"`.`"+RuleDirectionMapper.TABLE_NAME+"`" +
				" (`"+RuleDirectionMapper.ACCOUNT+"`,`"+RuleDirectionMapper.DIRECTION_TYPE+"`," +
				" `"+RuleDirectionMapper.DOMAIN+"`,`"+RuleDirectionMapper.FREE_VALUE+"`," +
				" `"+RuleDirectionMapper.GROUP_NAME+"`, `"+RuleDirectionMapper.RULE_ID+"`)" +
				" VALUES(:account,:directionType,:domain,:freeValue,:groupName,:ruleId);";
		MapSqlParameterSource source = new MapSqlParameterSource("account",direction.getAccount());
		source.addValue("directionType", direction.getDirectionType());
		source.addValue("domain", direction.getDomain());
		source.addValue("freeValue", direction.getFreeValue());
		source.addValue("groupName", direction.getGroup());
		source.addValue("ruleId", ruleId);
		template.update(sql, source, directionKey);
		return directionKey.getKey().intValue();
	}
	
	private void insertProperty(RulePropertyVO property, Long ruleId){
		String sql = "INSERT INTO `"+RulePropertyMapper.DATABASE+"`.`"+RulePropertyMapper.TABLE_NAME+"`" +
				" (`"+RulePropertyMapper.PROPERTY_KEY+"`,`"+RulePropertyMapper.PROPERTY_VALUE+"`,`"+RulePropertyMapper.RULE_ID+"`)" +
				" VALUES(:key,:value,:ruleId)";
		MapSqlParameterSource source = new MapSqlParameterSource("ruleId",ruleId);
		source.addValue("key",property.getPropertyKey());
		source.addValue("value", property.getPropertyValue().replace("\r\n", "\n").replace("\r", "\n"));
		template.update(sql, source);
	}
	
	
	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void update(RuleVO rule) {
		String sql = "UPDATE `"+RuleMapper.DATABASE+"`.`"+RuleMapper.TABLE_NAME+"` " +
				" SET `"+RuleMapper.ADMIN_ORDER+"` = :adminOrder ,`"+RuleMapper.LABEL+"` = :label," +
				" `"+RuleMapper.TWO_WAYS+"` = :twoWays,`"+RuleMapper.UPDATED+"` = :updated" +
				" WHERE `"+RuleMapper.ID+"` = :ruleId;";
		MapSqlParameterSource source = new MapSqlParameterSource("ruleId",rule.getId());
		source.addValue("updated", Calendar.getInstance().getTime());
		source.addValue("label", rule.getName());
		source.addValue("twoWays", rule.getTwoWays());
		source.addValue("adminOrder", rule.getAdminOrder());
		template.update(sql, source);
		updateDirection(rule.getFromDirection());
		updateDirection(rule.getToDirection());
		String propertiesSql = "DELETE FROM `"+RulePropertyMapper.DATABASE+"`.`"+RulePropertyMapper.TABLE_NAME+"`" +
				" WHERE `"+RulePropertyMapper.RULE_ID+"` = :ruleId ;";
		template.update(propertiesSql, new MapSqlParameterSource("ruleId",rule.getId()));
		if(rule.getProperties()!=null && rule.getProperties().size()>0){
			for(RulePropertyVO property : rule.getProperties()){
				insertProperty(property,rule.getId());
			}
		}
	}

	private void updateDirection(RuleDirectionVO direction){
		String sql = "UPDATE `"+RuleDirectionMapper.DATABASE+"`.`"+RuleDirectionMapper.TABLE_NAME+"`" +
				" SET `"+RuleDirectionMapper.ACCOUNT+"` = :account, `"+RuleDirectionMapper.DIRECTION_TYPE+"` = :directionType," +
				" `"+RuleDirectionMapper.DOMAIN+"` = :domain, `"+RuleDirectionMapper.FREE_VALUE+"` = :freeValue," +
				" `"+RuleDirectionMapper.GROUP_NAME+"` = :groupName" +
				" WHERE `"+RuleDirectionMapper.ID+"` = :directionId ;";
		MapSqlParameterSource source = new MapSqlParameterSource("account",direction.getAccount());
		source.addValue("directionType", direction.getDirectionType());
		source.addValue("domain", direction.getDomain());
		source.addValue("freeValue", direction.getFreeValue());
		source.addValue("groupName", direction.getGroup());
		source.addValue("directionId", direction.getId());
		template.update(sql, source);
	}

	
	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void toggleStatus(Long ruleId) {
		String sql = "UPDATE `"+RuleMapper.DATABASE+"`.`"+RuleMapper.TABLE_NAME+"`" +
				" SET `"+RuleMapper.ENABLED+"` = NOT `"+RuleMapper.ENABLED+"`" +
				" WHERE `"+RuleMapper.ID+"` = :ruleId ;";
		template.update(sql, new MapSqlParameterSource("ruleId",ruleId));
	}

	@Override
	public List<RuleVO> findByDomainId(String domain, String component) {
		String sql = SELECT;		
		String where = null;
		MapSqlParameterSource source = new MapSqlParameterSource();
		if(component!=null){
			
			where = " WHERE  `"+FeatureMapper.TABLE_NAME+"`.`"+FeatureMapper.COMPONENT+"` = :component ";
			source.addValue("component", component);
		}
		if(domain!=null){
			if(where == null){
				where = " WHERE ";
			}else{
				where = where+" AND ";
			}
			where = where + " `"+RuleMapper.TABLE_NAME+"`.`"+RuleMapper.DOMAIN_ID+"` = :domain ";
			source.addValue("domain", domain);
		}
		List<RuleVO> rules = null;
		if(where==null){
			rules = template.query(sql+where, source, new RuleMapper());
		}else{
			rules = template.getJdbcOperations().query(sql, new RuleMapper());
		}
		
		if(rules!=null && rules.size()>0){
			for(RuleVO rule : rules){
				rule.setToDirection(findDirectionById(rule.getToDirection().getId()));
				rule.setFromDirection(findDirectionById(rule.getFromDirection().getId()));
				rule.setProperties(findPropertiesByRuleId(rule.getId()));
			}
			return rules;
		}
		return null;
	}

	@Override
	public List<RuleVO> findWitNullDomain(String component) {
		String sql = SELECT +
				" WHERE `"+RuleMapper.DOMAIN_ID+"` IS NULL" +
				" AND `"+RuleMapper.FEATURE_ID+"` = :component ;";
		String where = null;
		MapSqlParameterSource source = new MapSqlParameterSource();
		if(component!=null){
			sql = sql + " INNER JOIN `"+FeatureMapper.DATABASE+"`.`"+FeatureMapper.TABLE_NAME+"` "
					+" ON `"+FeatureMapper.TABLE_NAME+"`.`"+FeatureMapper.ID+"` = `"+RuleMapper.TABLE_NAME+"`.`"+RuleMapper.FEATURE_ID+"` ";
			where = " AND  `"+FeatureMapper.TABLE_NAME+"`.`"+FeatureMapper.COMPONENT+"` = :component ";
			source.addValue("component", component);
		}
		List<RuleVO> rules = null;
		if(where==null){
			rules = template.query(sql+where, source, new RuleMapper());
		}else{
			rules = template.getJdbcOperations().query(sql, new RuleMapper());
		}
		if(rules!=null && rules.size()>0){
			for(RuleVO rule : rules){
				rule.setToDirection(findDirectionById(rule.getToDirection().getId()));
				rule.setFromDirection(findDirectionById(rule.getFromDirection().getId()));
				rule.setProperties(findPropertiesByRuleId(rule.getId()));
			}
			return rules;
		}
		return null;
	}
	
	private RuleDirectionVO findDirectionById(Integer directionId){
		String sql = "SELECT `"+RuleDirectionMapper.ACCOUNT+"`, `"+RuleDirectionMapper.DIRECTION_TYPE+"`," +
				" `"+RuleDirectionMapper.DOMAIN+"`, `"+RuleDirectionMapper.FREE_VALUE+"`," +
				" `"+RuleDirectionMapper.GROUP_NAME+"`, `"+RuleDirectionMapper.ID+"`" +
				" FROM `"+RuleDirectionMapper.DATABASE+"`.`"+RuleDirectionMapper.TABLE_NAME+"`" +
				" WHERE `"+RuleDirectionMapper.ID+"` = :directionId";
		MapSqlParameterSource source = new MapSqlParameterSource("directionId",directionId);
		List<RuleDirectionVO> directions = template.query(sql, source, new RuleDirectionMapper());
		if(directions!=null && directions.size()>0){
			return directions.get(0);
		}
		return null;
	}

	private List<RulePropertyVO> findPropertiesByRuleId(Long ruleId){
		String sql = "SELECT `"+RulePropertyMapper.ID+"`, `"+RulePropertyMapper.PROPERTY_KEY+"`, `"+RulePropertyMapper.PROPERTY_VALUE+"`" +
				" FROM `"+RulePropertyMapper.DATABASE+"`.`"+RulePropertyMapper.TABLE_NAME+"`" +
				" WHERE `"+RulePropertyMapper.RULE_ID+"` = :ruleId";
		return template.query(sql, new MapSqlParameterSource("ruleId",ruleId), new RulePropertyMapper());
	}

	@Override
	public RuleVO findById(Long ruleId) {
		String sql = SELECT + " WHERE `"+RuleMapper.ID+"` = :ruleId";
		MapSqlParameterSource source = new MapSqlParameterSource("ruleId",ruleId);
		List<RuleVO> rules = template.query(sql, source, new RuleMapper());
		if(rules!=null && rules.size()>0){
			return rules.get(0);
		}
		return null;
	}
}

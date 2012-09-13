/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.console.backend.repository.jdbc;

import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.mxhero.console.backend.repository.FeatureRuleRepository;
import org.mxhero.console.backend.repository.jdbc.mapper.FeatureRuleDirectionMapper;
import org.mxhero.console.backend.repository.jdbc.mapper.FeatureRuleMapper;
import org.mxhero.console.backend.repository.jdbc.mapper.FeatureRulePropertyMapper;
import org.mxhero.console.backend.vo.FeatureRuleDirectionVO;
import org.mxhero.console.backend.vo.FeatureRulePropertyVO;
import org.mxhero.console.backend.vo.FeatureRuleVO;
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
public class JdbcFeatureRuleRepository implements FeatureRuleRepository{

	private static final String SELECT = "SELECT `"+FeatureRuleMapper.ADMIN_ORDER+"`," +
			" `"+FeatureRuleMapper.CREATED+"`, `"+FeatureRuleMapper.ENABLED+"`," +
			" `"+FeatureRuleMapper.FROM_DIRECTION_ID+"`, `"+FeatureRuleMapper.ID+"`," +
			" `"+FeatureRuleMapper.LABEL+"`, `"+FeatureRuleMapper.TO_DIRECTION_ID+"`," +
			" `"+FeatureRuleMapper.TWO_WAYS+"`,`"+FeatureRuleMapper.UPDATED+"`," +
			" `"+FeatureRuleMapper.DOMAIN_ID+"`, `"+FeatureRuleMapper.FEATURE_ID+"`" +
			" FROM `"+FeatureRuleMapper.DATABASE+"`.`"+FeatureRuleMapper.TABLE_NAME+"`";
	
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcFeatureRuleRepository(@Qualifier("mxheroDataSource")DataSource ds) {
		this.template =  new NamedParameterJdbcTemplate(ds);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void delete(Integer ruleId) {
		String propertiesSql = "DELETE FROM `"+FeatureRulePropertyMapper.DATABASE+"`.`"+FeatureRulePropertyMapper.TABLE_NAME+"`" +
				" WHERE `"+FeatureRulePropertyMapper.RULE_ID+"` = :ruleId ;";
		template.update(propertiesSql, new MapSqlParameterSource("ruleId",ruleId));
		
		String directionSql = "DELETE FROM `"+FeatureRuleDirectionMapper.DATABASE+"`.`"+FeatureRuleDirectionMapper.TABLE_NAME+"`" +
				" WHERE `"+FeatureRuleDirectionMapper.RULE_ID+"` = :ruleId ;";
		template.update(directionSql, new MapSqlParameterSource("ruleId",ruleId));	
		
		String ruleSql = "DELETE FROM `"+FeatureRuleMapper.DATABASE+"`.`"+FeatureRuleMapper.TABLE_NAME+"`" +
				" WHERE `"+FeatureRuleMapper.ID+"` = :ruleId ;";
		template.update(ruleSql, new MapSqlParameterSource("ruleId",ruleId));			
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void deleteByDomain(String domainId) {
		String idsSql = "SELECT DISTINCT `"+FeatureRuleDirectionMapper.RULE_ID+"` " +
				" FROM `"+FeatureRuleDirectionMapper.DATABASE+"`.`"+FeatureRuleDirectionMapper.TABLE_NAME+"` " +
				" WHERE `"+FeatureRuleDirectionMapper.DOMAIN+"` = :domainId ;";
		List<Integer> ids = template.queryForList(idsSql, new MapSqlParameterSource("domainId",domainId), Integer.class);
		if(ids!=null && ids.size()>0){
			for(Integer id : ids){
				delete(id);
			}
		}
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void deleteByAccount(String domainId, String accountId) {
		String idsSql = "SELECT DISTINCT `"+FeatureRuleDirectionMapper.RULE_ID+"` " +
				" FROM `"+FeatureRuleDirectionMapper.DATABASE+"`.`"+FeatureRuleDirectionMapper.TABLE_NAME+"` " +
				" WHERE `"+FeatureRuleDirectionMapper.DOMAIN+"` = :domainId " +
				" AND `"+FeatureRuleDirectionMapper.ACCOUNT+"` = :accountId ;";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",domainId);
		source.addValue("accountId", accountId);
		List<Integer> ids = template.queryForList(idsSql, source, Integer.class);
		if(ids!=null && ids.size()>0){
			for(Integer id : ids){
				delete(id);
			}
		}
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void deleteByGroup(String domainId, String group) {
		String idsSql = "SELECT DISTINCT `"+FeatureRuleDirectionMapper.RULE_ID+"` " +
				" FROM `"+FeatureRuleDirectionMapper.DATABASE+"`.`"+FeatureRuleDirectionMapper.TABLE_NAME+"` " +
				" WHERE `"+FeatureRuleDirectionMapper.DOMAIN+"` = :domainId " +
				" AND `"+FeatureRuleDirectionMapper.GROUP_NAME+"` = :group ;";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",domainId);
		source.addValue("group", group);
		List<Integer> ids = template.queryForList(idsSql, source, Integer.class);
		if(ids!=null && ids.size()>0){
			for(Integer id : ids){
				delete(id);
			}
		}
	}

	@Override
	public boolean checkFromTo(String domainId, Integer featureId,
			String fromFreeValue, String toFreeValue, Integer ruleId) {
		String sql = "SELECT SUM(1) FROM `"+FeatureRuleMapper.DATABASE+"`.`"+FeatureRuleMapper.TABLE_NAME+"` rules" +
				" INNER JOIN `"+FeatureRuleDirectionMapper.DATABASE+"`.`"+FeatureRuleDirectionMapper.TABLE_NAME+"` from_directions" +
				" ON from_directions.`"+FeatureRuleDirectionMapper.ID+"` = rules.`"+FeatureRuleMapper.FROM_DIRECTION_ID+"`" +
				" INNER JOIN `"+FeatureRuleDirectionMapper.DATABASE+"`.`"+FeatureRuleDirectionMapper.TABLE_NAME+"` to_directions" +
				" ON to_directions.`"+FeatureRuleDirectionMapper.ID+"` = rules.`"+FeatureRuleMapper.TO_DIRECTION_ID+"`" +
				" WHERE to_directions.`"+FeatureRuleDirectionMapper.FREE_VALUE+"` = :toFreeValue" +
				" AND from_directions.`"+FeatureRuleDirectionMapper.FREE_VALUE+"` = :fromFreeValue" +
				" AND rules.`"+FeatureRuleMapper.FEATURE_ID+"` = :featureId"; 
		
		MapSqlParameterSource source = new MapSqlParameterSource("featureId",featureId);
		source.addValue("toFreeValue", toFreeValue);
		source.addValue("fromFreeValue", fromFreeValue);
		
		if(ruleId!=null){
			sql = sql + " AND rules.`"+FeatureRuleMapper.ID+"` <> :ruleId";
			source.addValue("ruleId", ruleId);
		}

		if(domainId!=null){
			sql = sql + " AND rules.`"+FeatureRuleMapper.DOMAIN_ID+"` = :domainId ;";
			source.addValue("domainId", domainId);
		}else{
			sql = sql + " AND rules.`"+FeatureRuleMapper.DOMAIN_ID+"` IS NULL ;";
		}
		return template.queryForInt(sql, source) > 0;
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public Integer insert(FeatureRuleVO rule) {
		String ruleInsertSql = "INSERT INTO `"+FeatureRuleMapper.DATABASE+"`.`"+FeatureRuleMapper.TABLE_NAME+"`" +
				" (`"+FeatureRuleMapper.ADMIN_ORDER+"`,`"+FeatureRuleMapper.CREATED+"`,`"+FeatureRuleMapper.DOMAIN_ID+"`," +
				" `"+FeatureRuleMapper.ENABLED+"`,`"+FeatureRuleMapper.FEATURE_ID+"`,`"+FeatureRuleMapper.LABEL+"`," +
				" `"+FeatureRuleMapper.TWO_WAYS+"`,`"+FeatureRuleMapper.UPDATED+"`)" +
				" VALUES(:adminOrder,:created,:domainId,:enabled,:featureId,:label,:twoWays,:updated) ;";
		KeyHolder ruleKey = new GeneratedKeyHolder();
		MapSqlParameterSource ruleSource = new MapSqlParameterSource("adminOrder",rule.getAdminOrder());
		ruleSource.addValue("created", Calendar.getInstance().getTime());
		ruleSource.addValue("domainId", rule.getDomain());
		ruleSource.addValue("enabled", true);
		ruleSource.addValue("featureId", rule.getFeatureId());
		ruleSource.addValue("label", rule.getName());
		ruleSource.addValue("twoWays", rule.getTwoWays());
		ruleSource.addValue("updated", Calendar.getInstance().getTime());
		template.update(ruleInsertSql, ruleSource, ruleKey);
		Integer fromDirectionId = insertDirection(rule.getFromDirection(),ruleKey.getKey().intValue());
		Integer toDirectionId = insertDirection(rule.getToDirection(),ruleKey.getKey().intValue());
		String updateDirectionsSql = "UPDATE `"+FeatureRuleMapper.DATABASE+"`.`"+FeatureRuleMapper.TABLE_NAME+"`" +
				" SET `"+FeatureRuleMapper.FROM_DIRECTION_ID+"` = :fromDirectionId ," +
				" `"+FeatureRuleMapper.TO_DIRECTION_ID+"` = :toDirectionId" +
				" WHERE `"+FeatureRuleMapper.ID+"` = :ruleId";
		MapSqlParameterSource ruleDirectionsSource = new MapSqlParameterSource("fromDirectionId",fromDirectionId);
		ruleDirectionsSource.addValue("toDirectionId",toDirectionId);
		ruleDirectionsSource.addValue("ruleId", ruleKey.getKey().intValue());
		template.update(updateDirectionsSql, ruleDirectionsSource);
		if(rule.getProperties()!=null && rule.getProperties().size()>0){
			for(FeatureRulePropertyVO property : rule.getProperties()){
				insertProperty(property,ruleKey.getKey().intValue());
			}
		}
		return ruleKey.getKey().intValue();
	}

	private Integer insertDirection(FeatureRuleDirectionVO direction, Integer ruleId){
		KeyHolder directionKey = new GeneratedKeyHolder();
		String sql = "INSERT INTO `"+FeatureRuleDirectionMapper.DATABASE+"`.`"+FeatureRuleDirectionMapper.TABLE_NAME+"`" +
				" (`"+FeatureRuleDirectionMapper.ACCOUNT+"`,`"+FeatureRuleDirectionMapper.DIRECTION_TYPE+"`," +
				" `"+FeatureRuleDirectionMapper.DOMAIN+"`,`"+FeatureRuleDirectionMapper.FREE_VALUE+"`," +
				" `"+FeatureRuleDirectionMapper.GROUP_NAME+"`, `"+FeatureRuleDirectionMapper.RULE_ID+"`)" +
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
	
	private void insertProperty(FeatureRulePropertyVO property, Integer ruleId){
		String sql = "INSERT INTO `"+FeatureRulePropertyMapper.DATABASE+"`.`"+FeatureRulePropertyMapper.TABLE_NAME+"`" +
				" (`"+FeatureRulePropertyMapper.PROPERTY_KEY+"`,`"+FeatureRulePropertyMapper.PROPERTY_VALUE+"`,`"+FeatureRulePropertyMapper.RULE_ID+"`)" +
				" VALUES(:key,:value,:ruleId)";
		MapSqlParameterSource source = new MapSqlParameterSource("ruleId",ruleId);
		source.addValue("key",property.getPropertyKey());
		source.addValue("value", property.getPropertyValue().replace("\r\n", "\n").replace("\r", "\n"));
		template.update(sql, source);
	}
	
	
	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void update(FeatureRuleVO rule) {
		String sql = "UPDATE `"+FeatureRuleMapper.DATABASE+"`.`"+FeatureRuleMapper.TABLE_NAME+"` " +
				" SET `"+FeatureRuleMapper.ADMIN_ORDER+"` = :adminOrder ,`"+FeatureRuleMapper.LABEL+"` = :label," +
				" `"+FeatureRuleMapper.TWO_WAYS+"` = :twoWays,`"+FeatureRuleMapper.UPDATED+"` = :updated" +
				" WHERE `"+FeatureRuleMapper.ID+"` = :ruleId;";
		MapSqlParameterSource source = new MapSqlParameterSource("ruleId",rule.getId());
		source.addValue("updated", Calendar.getInstance().getTime());
		source.addValue("label", rule.getName());
		source.addValue("twoWays", rule.getTwoWays());
		source.addValue("adminOrder", rule.getAdminOrder());
		template.update(sql, source);
		updateDirection(rule.getFromDirection());
		updateDirection(rule.getToDirection());
		String propertiesSql = "DELETE FROM `"+FeatureRulePropertyMapper.DATABASE+"`.`"+FeatureRulePropertyMapper.TABLE_NAME+"`" +
				" WHERE `"+FeatureRulePropertyMapper.RULE_ID+"` = :ruleId ;";
		template.update(propertiesSql, new MapSqlParameterSource("ruleId",rule.getId()));
		if(rule.getProperties()!=null && rule.getProperties().size()>0){
			for(FeatureRulePropertyVO property : rule.getProperties()){
				insertProperty(property,rule.getId());
			}
		}
	}

	private void updateDirection(FeatureRuleDirectionVO direction){
		String sql = "UPDATE `"+FeatureRuleDirectionMapper.DATABASE+"`.`"+FeatureRuleDirectionMapper.TABLE_NAME+"`" +
				" SET `"+FeatureRuleDirectionMapper.ACCOUNT+"` = :account, `"+FeatureRuleDirectionMapper.DIRECTION_TYPE+"` = :directionType," +
				" `"+FeatureRuleDirectionMapper.DOMAIN+"` = :domain, `"+FeatureRuleDirectionMapper.FREE_VALUE+"` = :freeValue," +
				" `"+FeatureRuleDirectionMapper.GROUP_NAME+"` = :groupName" +
				" WHERE `"+FeatureRuleDirectionMapper.ID+"` = :directionId ;";
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
	public void toggleStatus(Integer ruleId) {
		String sql = "UPDATE `"+FeatureRuleMapper.DATABASE+"`.`"+FeatureRuleMapper.TABLE_NAME+"`" +
				" SET `"+FeatureRuleMapper.ENABLED+"` = NOT `"+FeatureRuleMapper.ENABLED+"`" +
				" WHERE `"+FeatureRuleMapper.ID+"` = :ruleId ;";
		template.update(sql, new MapSqlParameterSource("ruleId",ruleId));
	}

	@Override
	public List<FeatureRuleVO> findByDomainId(String domainId, Integer featureId) {
		String sql = SELECT +
				" WHERE `"+FeatureRuleMapper.DOMAIN_ID+"` = :domainId" +
				" AND `"+FeatureRuleMapper.FEATURE_ID+"` = :featureId ;";
		MapSqlParameterSource source = new MapSqlParameterSource("featureId",featureId);
		source.addValue("domainId", domainId);
		List<FeatureRuleVO> rules = template.query(sql, source, new FeatureRuleMapper());
		if(rules!=null && rules.size()>0){
			for(FeatureRuleVO rule : rules){
				rule.setToDirection(findDirectionById(rule.getToDirection().getId()));
				rule.setFromDirection(findDirectionById(rule.getFromDirection().getId()));
				rule.setProperties(findPropertiesByRuleId(rule.getId()));
			}
			return rules;
		}
		return null;
	}

	@Override
	public List<FeatureRuleVO> findWitNullDomain(Integer featureId) {
		String sql = SELECT +
				" WHERE `"+FeatureRuleMapper.DOMAIN_ID+"` IS NULL" +
				" AND `"+FeatureRuleMapper.FEATURE_ID+"` = :featureId ;";
		MapSqlParameterSource source = new MapSqlParameterSource("featureId",featureId);
		List<FeatureRuleVO> rules = template.query(sql, source, new FeatureRuleMapper());
		if(rules!=null && rules.size()>0){
			for(FeatureRuleVO rule : rules){
				rule.setToDirection(findDirectionById(rule.getToDirection().getId()));
				rule.setFromDirection(findDirectionById(rule.getFromDirection().getId()));
				rule.setProperties(findPropertiesByRuleId(rule.getId()));
			}
			return rules;
		}
		return null;
	}
	
	private FeatureRuleDirectionVO findDirectionById(Integer directionId){
		String sql = "SELECT `"+FeatureRuleDirectionMapper.ACCOUNT+"`, `"+FeatureRuleDirectionMapper.DIRECTION_TYPE+"`," +
				" `"+FeatureRuleDirectionMapper.DOMAIN+"`, `"+FeatureRuleDirectionMapper.FREE_VALUE+"`," +
				" `"+FeatureRuleDirectionMapper.GROUP_NAME+"`, `"+FeatureRuleDirectionMapper.ID+"`" +
				" FROM `"+FeatureRuleDirectionMapper.DATABASE+"`.`"+FeatureRuleDirectionMapper.TABLE_NAME+"`" +
				" WHERE `"+FeatureRuleDirectionMapper.ID+"` = :directionId";
		MapSqlParameterSource source = new MapSqlParameterSource("directionId",directionId);
		List<FeatureRuleDirectionVO> directions = template.query(sql, source, new FeatureRuleDirectionMapper());
		if(directions!=null && directions.size()>0){
			return directions.get(0);
		}
		return null;
	}

	private List<FeatureRulePropertyVO> findPropertiesByRuleId(Integer ruleId){
		String sql = "SELECT `"+FeatureRulePropertyMapper.ID+"`, `"+FeatureRulePropertyMapper.PROPERTY_KEY+"`, `"+FeatureRulePropertyMapper.PROPERTY_VALUE+"`" +
				" FROM `"+FeatureRulePropertyMapper.DATABASE+"`.`"+FeatureRulePropertyMapper.TABLE_NAME+"`" +
				" WHERE `"+FeatureRulePropertyMapper.RULE_ID+"` = :ruleId";
		return template.query(sql, new MapSqlParameterSource("ruleId",ruleId), new FeatureRulePropertyMapper());
	}

	@Override
	public FeatureRuleVO findById(Integer ruleId) {
		String sql = SELECT + " WHERE `"+FeatureRuleMapper.ID+"` = :ruleId";
		MapSqlParameterSource source = new MapSqlParameterSource("ruleId",ruleId);
		List<FeatureRuleVO> rules = template.query(sql, source, new FeatureRuleMapper());
		if(rules!=null && rules.size()>0){
			return rules.get(0);
		}
		return null;
	}
}

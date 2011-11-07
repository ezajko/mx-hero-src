package org.mxhero.engine.plugin.featuresfp.internal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.sql.DataSource;

import org.mxhero.engine.commons.feature.Feature;
import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleDirection;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.feature.RulesFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;


public class JdbcRulesFinder implements RulesFinder{

	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcRulesFinder(DataSource ds){
		this.template = new NamedParameterJdbcTemplate(ds);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Feature find(String componet, Integer version) {
		Feature feature = null;
		String featureSql = "SELECT id, base_priority, component, version " +
				" FROM features WHERE component = :component AND version = :version;";
		MapSqlParameterSource featureParams = new MapSqlParameterSource();
		featureParams.addValue("component", componet);
		featureParams.addValue("version", version);
		List<Feature> features = template.query(featureSql, featureParams, new RowMapper<Feature>() {
			@Override
			public Feature mapRow(ResultSet rs, int rowNum) throws SQLException {
				Feature feature = new Feature();
				feature.setId(rs.getInt("id"));
				feature.setBasePriority(rs.getInt("base_priority"));
				feature.setComponent(rs.getString("component"));
				feature.setRules(new HashSet<Rule>());
				feature.setVersion(rs.getInt("version"));
				return feature;
			}
		});
		if(features!=null && features.size()>0){
			feature=features.get(0);
			feature.getRules().addAll(getRules(feature.getId()));
		}
		
		return feature;
	}

	private Collection<Rule> getRules(Integer ruleId){
		String rulesSql = "SELECT id, admin_order, domain_id, enabled, two_ways, from_direction_id, to_direction_id " +
				" FROM features_rules WHERE feature_id = :featureId";
		Collection<Rule> rules = template.query(rulesSql, new MapSqlParameterSource("featureId",ruleId) , new RowMapper<Rule>() {
			@Override
			public Rule mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Rule rule = new Rule();
				rule.setAdminOrder(rs.getString("admin_order"));
				rule.setDomain(rs.getString("domain_id"));
				rule.setEnabled(rs.getBoolean("enabled"));
				rule.setFromDirection(new RuleDirection());
				rule.getFromDirection().setId(rs.getInt("from_direction_id"));
				rule.setId(rs.getInt("id"));
				rule.setToDirection(new RuleDirection());
				rule.getToDirection().setId(rs.getInt("to_direction_id"));
				rule.setTwoWays(rs.getBoolean("two_ways"));
				return rule;
			}
		});
		
		for(Rule rule : rules){
			rule.setProperties(getProperties(rule.getId()));
			rule.setFromDirection(getDirection(rule.getFromDirection().getId()));
			rule.setToDirection(getDirection(rule.getToDirection().getId()));
		}
		return rules;
	}
	
	
	private Collection<RuleProperty> getProperties(Integer ruleId){
		Collection<RuleProperty> properties;
		String sqlProperties = "SELECT property_key, property_value " +
				" FROM features_rules_properties " +
				" WHERE rule_id = :ruleId";
		properties=template.query(sqlProperties, new MapSqlParameterSource("ruleId",ruleId), new RowMapper<RuleProperty>() {
			@Override
			public RuleProperty mapRow(ResultSet rs, int rowNum) throws SQLException {
				RuleProperty property = new RuleProperty();
				property.setPropertyKey(rs.getString("property_key"));
				property.setPropertyValue(rs.getString("property_value"));
				return property;
			}
		});
		return properties;
	}
	
	private RuleDirection getDirection(Integer directionId){
		String sqlDirection = "SELECT id, account, directiom_type, domain, free_value, group_name " +
				" FROM features_rules_directions WHERE id = :directionId";
		List<RuleDirection> directions = template.query(sqlDirection, new MapSqlParameterSource("directionId",directionId), new RowMapper<RuleDirection>() {
			@Override
			public RuleDirection mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				RuleDirection direction = new RuleDirection();
				direction.setAccount(rs.getString("account"));
				direction.setDirectionType(rs.getString("directiom_type"));
				direction.setDomain(rs.getString("domain"));
				direction.setGroup(rs.getString("group_name"));
				direction.setFreeValue(rs.getString("free_value"));
				direction.setId(rs.getInt("id"));
				return direction;
			}
		});
		if(directions!=null && directions.size()>0){
			return directions.get(0);
		}
		return null;
	}
}

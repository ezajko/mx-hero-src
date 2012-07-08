package org.mxhero.engine.plugin.disclaimercontract.internal.repository.jdbc;

import java.util.List;

import javax.sql.DataSource;

import org.mxhero.engine.plugin.disclaimercontract.entity.Contract;
import org.mxhero.engine.plugin.disclaimercontract.entity.Request;
import org.mxhero.engine.plugin.disclaimercontract.internal.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcContractRepository implements ContractRepository{

	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcContractRepository(DataSource ds) {
		this.template = new NamedParameterJdbcTemplate(ds);
	}

	@Override
	public List<Contract> findByRule(Long ruleId) {
		String sql = "SELECT * FROM "+ContractMapper.TABLE_NAME
				+" WHERE "+ContractMapper.RULE_ID+"= :ruleId";
		MapSqlParameterSource source = new MapSqlParameterSource("ruleId",ruleId);
		return template.query(sql, source, new ContractMapper());
	}

	@Override
	public Contract findByRuleAndRecipient(Long ruleId, String recipient) {
		String sql = "SELECT * FROM "+ContractMapper.TABLE_NAME
				+" WHERE "+ContractMapper.RULE_ID+"= :ruleId AND "+ContractMapper.RECIPIENT+" = :recipient";
		MapSqlParameterSource source = new MapSqlParameterSource("ruleId",ruleId);
		source.addValue("recipient", recipient);
		List<Contract> results = template.query(sql, source, new ContractMapper());
		if(results!=null && results.size()>0){
			return results.get(0);
		}
		return null;
	}

	@Override
	public Request addRequest(Request request) {
		String sql = "INSERT INTO `"+RequestMapper.TABLE_NAME+"` " 
				+" (`"+RequestMapper.DISCLAIMER_HTML+"`,`"+RequestMapper.DISCLAIMER_PLAIN+"`," +
				"`"+RequestMapper.MESSAGE_ID+"`,`"+RequestMapper.PENDING+"`," +
				"`"+RequestMapper.RECIPIENT+"`,`"+RequestMapper.REQUEST_DATE+"`," +
				"`"+RequestMapper.RULE_ID+"`,`"+RequestMapper.SENDER_DOMAIN+"`, " +
				"`"+RequestMapper.PATH+"`,`"+RequestMapper.PHASE+"`, " +
				"`"+RequestMapper.RULE_PRIORITY+"`)" +
				" VALUES (:disclaimerHtml,:disclaimerPlain,:messageId,:pending,:recipient,:requestDate,:ruleId,:senderDomain,:path,:phase,:priority)";
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("disclaimerHtml", request.getDisclaimerHtml());
		source.addValue("disclaimerPlain", request.getDisclaimerPlain());
		source.addValue("messageId", request.getMessageId());
		source.addValue("pending", request.getPending());
		source.addValue("recipient", request.getRecipient());
		source.addValue("requestDate", request.getRequestDate());
		source.addValue("ruleId", request.getRuleId());
		source.addValue("senderDomain", request.getSenderDomain());
		source.addValue("path", request.getPath());
		source.addValue("phase", request.getPhase());
		source.addValue("priority", request.getRulePriority());
		KeyHolder id = new GeneratedKeyHolder();
		template.update(sql, source, id);
		if(id.getKey()==null){
			throw new RuntimeException("no id returned for "+request.getMessageId()+" and recipient "+request.getRecipient());
		}
		return findRequestById(id.getKey().longValue());
	}

	@Override
	public List<Request> pending() {
		String sql = "SELECT * FROM `"+RequestMapper.TABLE_NAME+"` WHERE `"+RequestMapper.PENDING+"` is TRUE ";
		List<Request> results = template.getJdbcOperations().query(sql, new RequestMapper());
		return results;
	}

	@Override
	public Contract create(Contract contract) {
		String sql = "INSERT INTO `"+ContractMapper.TABLE_NAME+"`" 
				+" (`"+ContractMapper.RECIPIENT+"`,`"+ContractMapper.SENDER_DOMAIN+"`,"
				+" `"+ContractMapper.RULE_ID+"`,`"+ContractMapper.APPROVED_DATE+"`,"
				+" `"+ContractMapper.DISCLAIMER_PLAIN+"`,`"+ContractMapper.DISCLAIMER_HTML+"`,"
				+" `"+ContractMapper.ADITIONAL_DATA+"`) " +
				" VALUES (:recipient,:senderDomain,:ruleId,:approvedDate,:disclaimePlain,:disclaimerHtml,:aditionalData) ";
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("recipient", contract.getRecipient());
		source.addValue("senderDomain", contract.getSenderDomain());
		source.addValue("ruleId", contract.getRuleId());
		source.addValue("approvedDate", contract.getApprovedDate());
		source.addValue("disclaimePlain", contract.getDisclaimerPlain());
		source.addValue("disclaimerHtml", contract.getDisclaimerHtml());
		source.addValue("aditionalData", contract.getAditionalData());
		KeyHolder id = new GeneratedKeyHolder();
		template.update(sql, source, id);
		if(id.getKey()==null){
			throw new RuntimeException("no id returned for "+contract.getSenderDomain()+" and recipient "+contract.getRecipient());
		}
		return findByRuleAndRecipient(contract.getRuleId(), contract.getRecipient());
	}

	@Override
	public void markDone(Long requestId) {
		String sql = "UPDATE `"+RequestMapper.TABLE_NAME+"` " +
				" SET `"+RequestMapper.PENDING+"` = false " +
				" WHERE `"+RequestMapper.ID+"` = :requestId ";
		template.update(sql, new MapSqlParameterSource("requestId",requestId));
	}

	private Request findRequestById(Long id){
		String sql = "SELECT * FROM `"+RequestMapper.TABLE_NAME+"` WHERE `"+RequestMapper.ID+"` = :id ";
		Request request = null;
		List<Request> results = template.query(sql, new MapSqlParameterSource("id",id), new RequestMapper());
		if(results!=null && results.size()>0){
			request = results.get(0);
		}
		return request;
	}

	@Override
	public List<Request> oldNotAccepted(Integer hours) {
		String sql = "SELECT * FROM `"+RequestMapper.TABLE_NAME+
				"` WHERE `"+RequestMapper.APPROVED_DATE+"` is NULL "+
				" AND DATE_ADD(`"+RequestMapper.REQUEST_DATE+ "`, INTERVAL :hours HOUR) < NOW() ";
		List<Request> results = template.query(sql, new MapSqlParameterSource("hours",hours), new RequestMapper());
		return results;
	}

	@Override
	public void remove(Long requestId) {
		template.update("DELETE FROM `"+RequestMapper.TABLE_NAME+"` WHERE `"+RequestMapper.ID+"` = :id ", new MapSqlParameterSource("id", requestId));
	}
}

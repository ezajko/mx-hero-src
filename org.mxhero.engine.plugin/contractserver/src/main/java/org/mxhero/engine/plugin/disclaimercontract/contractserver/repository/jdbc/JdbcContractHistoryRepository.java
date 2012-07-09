package org.mxhero.engine.plugin.disclaimercontract.contractserver.repository.jdbc;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.mxhero.engine.plugin.disclaimercontract.contractserver.pagination.PageResult;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.pagination.jdbc.BaseJdbcDao;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.pagination.jdbc.JdbcPageInfo;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.repository.ContractHistoryRepository;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.repository.jdbc.mapper.ContractHistoryMapper;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.repository.jdbc.mapper.ContractMapper;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.vo.ContractHistoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcContractHistoryRepository extends BaseJdbcDao<ContractHistoryVO> implements ContractHistoryRepository{

	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcContractHistoryRepository(DataSource ds) {
		this.template =  new NamedParameterJdbcTemplate(ds);
		super.setNamedParameterJdbcTemplate(template);
	}

	@Override
	public PageResult<ContractHistoryVO> readContractHistory(String senderDomain, String recipient, Integer limit, Integer offset) {
		String sql = "SELECT * FROM `"+ContractHistoryMapper.TABLE_NAME+"` ";
		String where =null;
		MapSqlParameterSource source = new MapSqlParameterSource();
		if(senderDomain!=null){
			where = "WHERE "+"`"+ContractMapper.SENDER_DOMAIN+"` = :senderDomain";
			source.addValue("senderDomain", senderDomain);
		}
		if(recipient!=null){
			if(where == null){
				where = "WHERE "+"`"+ContractMapper.RECIPIENT+"` like :recipient";
			}else{
				where = where +" AND `"+ContractMapper.RECIPIENT+"` like :recipient";
			}
			source.addValue("recipient", "%"+recipient+"%");
		}
		JdbcPageInfo pi = new JdbcPageInfo();
		pi.setOrderByList(new ArrayList<String>());
		pi.getOrderByList().add("`"+ContractMapper.APPROVED_DATE+"` DESC ");
		pi.getOrderByList().add("`"+ContractMapper.RULE_ID+"` DESC ");
		pi.getOrderByList().add("`"+ContractMapper.RECIPIENT+"` DESC ");
		pi.setPageNo(limit);
		pi.setPageSize(offset);
		pi.putRowMapper(new ContractHistoryMapper());
		pi.putSql((where==null)?sql:sql+where);
		pi.putExampleModel(source.getValues());
		PageResult<ContractHistoryVO> result = super.findByPage(pi);
		return result;
	}

}

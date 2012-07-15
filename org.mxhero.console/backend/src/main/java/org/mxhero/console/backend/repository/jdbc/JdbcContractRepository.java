package org.mxhero.console.backend.repository.jdbc;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.mxhero.console.backend.infrastructure.pagination.common.PageResult;
import org.mxhero.console.backend.infrastructure.pagination.jdbc.BaseJdbcDao;
import org.mxhero.console.backend.infrastructure.pagination.jdbc.JdbcPageInfo;
import org.mxhero.console.backend.repository.ContractRepository;
import org.mxhero.console.backend.repository.jdbc.mapper.ContractMapper;
import org.mxhero.console.backend.vo.ContractVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcContractRepository extends BaseJdbcDao<ContractVO> implements ContractRepository {

	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcContractRepository(@Qualifier("mxheroDataSource")DataSource ds) {
		this.template =  new NamedParameterJdbcTemplate(ds);
		super.setNamedParameterJdbcTemplate(template);
	}


	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void removeContract(Long id) {
		template.update("DELETE FROM `"+ContractMapper.DATABASE+"`.`"+ContractMapper.TABLE_NAME+"` WHERE `"+ContractMapper.ID+"` = :id ", new MapSqlParameterSource("id",id));
	}

	@Override
	@Transactional(value="mxhero",readOnly=true)
	public PageResult<ContractVO> readContracts(String senderDomain,
			String recipient, Integer limit, Integer offset) {
		String sql = "SELECT * FROM `"+ContractMapper.DATABASE+"`.`"+ContractMapper.TABLE_NAME+"` ";
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
		pi.putRowMapper(new ContractMapper());
		pi.putSql((where==null)?sql:sql+where);
		pi.putExampleModel(source.getValues());
		PageResult<ContractVO> result = super.findByPage(pi);
		return result;
	}

}

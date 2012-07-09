package org.mxhero.engine.plugin.disclaimercontract.contractserver.repository.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.mxhero.engine.plugin.disclaimercontract.contractserver.pagination.PageResult;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.pagination.jdbc.BaseJdbcDao;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.pagination.jdbc.JdbcPageInfo;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.repository.RequestRepository;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.repository.jdbc.mapper.ContractMapper;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.repository.jdbc.mapper.RequestMapper;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.vo.RequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcRequestRepository  extends BaseJdbcDao<RequestVO> implements RequestRepository{

	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcRequestRepository(DataSource ds) {
		this.template =  new NamedParameterJdbcTemplate(ds);
		super.setNamedParameterJdbcTemplate(template);
	}

	@Override
	public RequestVO readRequest(Long id) {
		String sql = "SELECT * FROM `"+RequestMapper.TABLE_NAME+"` WHERE `"+RequestMapper.ID+"` = :id ";
		List<RequestVO> results = template.query(sql, new MapSqlParameterSource("id",id), new RequestMapper());
		if(results!=null && results.size()>0){
			return results.get(0);
		}
		return null;
	}

	@Override
	public void updateRequest(RequestVO requestVO) {
		String sql = "UPDATE `"+RequestMapper.TABLE_NAME+"` " +
				" SET `"+RequestMapper.ADITIONAL_DATA+"` = :additionalData ," +
				"`"+RequestMapper.APPROVED_DATE+"` = :approvedDate ," +
				"`"+RequestMapper.PENDING+"` = :pending ," +
				"`"+RequestMapper.TYPE+"` = :type ," +
				"`"+RequestMapper.VETO_DATE+"` = :vetoDate " +
				" WHERE `"+RequestMapper.ID+"` = :id ";
		MapSqlParameterSource source = new MapSqlParameterSource("id",requestVO.getId());
		source.addValue("additionalData", requestVO.getAdditionalData());
		source.addValue("approvedDate", requestVO.getApprovedDate());
		source.addValue("pending", requestVO.getPending());
		source.addValue("type", requestVO.getType());
		source.addValue("vetoDate", requestVO.getVetoDate());
		template.update(sql, source);
	}

	@Override
	public PageResult<RequestVO> readRequests(String senderDomain,
			String recipient, Integer limit, Integer offset) {
		String sql = "SELECT * FROM `"+RequestMapper.TABLE_NAME+"` ";
		String where =null;
		MapSqlParameterSource source = new MapSqlParameterSource();
		if(senderDomain!=null){
			where = "WHERE "+"`"+RequestMapper.SENDER_DOMAIN+"` = :senderDomain";
			source.addValue("senderDomain", senderDomain);
		}
		if(recipient!=null){
			if(where == null){
				where = "WHERE "+"`"+RequestMapper.RECIPIENT+"` like :recipient";
			}else{
				where = where +" AND `"+RequestMapper.RECIPIENT+"` like :recipient";
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
		pi.putRowMapper(new RequestMapper());
		pi.putSql((where==null)?sql:sql+where);
		pi.putExampleModel(source.getValues());
		PageResult<RequestVO> result = super.findByPage(pi);
		return result;
	}

}

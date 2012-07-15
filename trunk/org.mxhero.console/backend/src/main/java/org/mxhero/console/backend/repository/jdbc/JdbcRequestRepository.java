package org.mxhero.console.backend.repository.jdbc;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.mxhero.console.backend.infrastructure.pagination.common.PageResult;
import org.mxhero.console.backend.infrastructure.pagination.jdbc.BaseJdbcDao;
import org.mxhero.console.backend.infrastructure.pagination.jdbc.JdbcPageInfo;
import org.mxhero.console.backend.repository.RequestRepository;
import org.mxhero.console.backend.repository.jdbc.mapper.ContractMapper;
import org.mxhero.console.backend.repository.jdbc.mapper.RequestMapper;
import org.mxhero.console.backend.vo.RequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcRequestRepository  extends BaseJdbcDao<RequestVO> implements RequestRepository{

	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcRequestRepository(@Qualifier("mxheroDataSource")DataSource ds) {
		this.template =  new NamedParameterJdbcTemplate(ds);
		super.setNamedParameterJdbcTemplate(template);
	}

	@Override
	@Transactional(value="mxhero",readOnly=true)
	public PageResult<RequestVO> readRequests(String senderDomain,
			String recipient, Integer limit, Integer offset) {
		String sql = "SELECT * FROM `"+RequestMapper.DATABASE+"`.`"+RequestMapper.TABLE_NAME+"` ";
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
		pi.getOrderByList().add("`"+RequestMapper.REQUEST_DATE+"` DESC ");
		pi.getOrderByList().add("`"+RequestMapper.RULE_ID+"` DESC ");
		pi.getOrderByList().add("`"+RequestMapper.RECIPIENT+"` DESC ");
		pi.setPageNo(limit);
		pi.setPageSize(offset);
		pi.putRowMapper(new RequestMapper());
		pi.putSql((where==null)?sql:sql+where);
		pi.putExampleModel(source.getValues());
		PageResult<RequestVO> result = super.findByPage(pi);
		return result;
	}

}

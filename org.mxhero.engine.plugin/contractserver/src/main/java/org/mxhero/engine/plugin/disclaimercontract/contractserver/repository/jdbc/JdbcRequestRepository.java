package org.mxhero.engine.plugin.disclaimercontract.contractserver.repository.jdbc;

import java.util.List;

import javax.sql.DataSource;

import org.mxhero.engine.plugin.disclaimercontract.contractserver.repository.RequestRepository;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.repository.jdbc.mapper.RequestMapper;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.vo.RequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcRequestRepository  implements RequestRepository{

	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcRequestRepository(DataSource ds) {
		this.template =  new NamedParameterJdbcTemplate(ds);
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

}

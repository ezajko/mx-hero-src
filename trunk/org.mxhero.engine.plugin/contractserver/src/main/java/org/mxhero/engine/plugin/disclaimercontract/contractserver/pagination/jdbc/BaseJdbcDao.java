package org.mxhero.engine.plugin.disclaimercontract.contractserver.pagination.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mxhero.engine.plugin.disclaimercontract.contractserver.pagination.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class BaseJdbcDao<T> {

	private static Logger log = LoggerFactory.getLogger(BaseJdbcDao.class);

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public PageResult<T> findByPage(JdbcPageInfo pageInfo) {
		int recordsNumber = countRecordsNumberByExample(pageInfo);

		if (pageInfo.getPageSize() <= 0) {
			List<T> all = findByExample(pageInfo);
			return new PageResult<T>(all, recordsNumber, 1, 1);
		}

		int pageAmount = (recordsNumber % pageInfo.getPageSize() > 0) ? (recordsNumber
				/ pageInfo.getPageSize() + 1)
				: (recordsNumber / pageInfo.getPageSize());
		int pageNo = pageInfo.getPageNo() > 0 ? pageInfo.getPageNo() : 1;

		if (pageNo > pageAmount) {
			pageNo = pageAmount;
		}

		if (pageInfo.getEnd() != null && pageInfo.getEnd().booleanValue()) {
			pageNo = 1;
		}
		if (pageInfo.getEnd() != null && !pageInfo.getEnd().booleanValue()) {
			pageNo = pageAmount;
		}

		int firstResult = (pageNo - 1) * pageInfo.getPageSize();
		List<T> pageData = findByExample(pageInfo, firstResult,
				pageInfo.getPageSize());

		return new PageResult<T>(pageData, recordsNumber, pageNo, pageAmount);
	}

	@SuppressWarnings("unchecked")
	private List<T> findByExample(JdbcPageInfo pageInfo,
			int... firstResultAndMaxResults) {
		String sql = pageInfo.getSql();
		if (pageInfo.getOrderByList() != null
				&& pageInfo.getOrderByList().size() > 0) {
			sql = sql + " order by";
			for (String exp : pageInfo.getOrderByList()) {
				sql = sql + " " + exp + " ,";
			}
			if (sql.endsWith(",")) {
				sql = sql.substring(0, sql.length() - 1);
			}
		}
		log.debug("===============SQL:" + sql);
		if (firstResultAndMaxResults != null
				&& firstResultAndMaxResults.length == 2) {
			return (List<T>) namedParameterJdbcTemplate.query(
					sql,
					new MapSqlParameterSource(pageInfo.getExampleModel()),
					new SplitPageResultSetExtractor(pageInfo.getRowMapper(),
							firstResultAndMaxResults[0],
							firstResultAndMaxResults[1]));
		} else {
			return namedParameterJdbcTemplate.query(
					sql,
					new MapSqlParameterSource(pageInfo.getExampleModel()), pageInfo.getRowMapper());
		}
	}

	class SplitPageResultSetExtractor implements ResultSetExtractor<List<T>> {
		private final int start;
		private final int len;
		private final RowMapper<T> rowMapper;

		public SplitPageResultSetExtractor(RowMapper<T> rowMapper, int start,
				int len) {
			this.rowMapper = rowMapper;
			this.start = start;
			this.len = len;
		}

		public List<T> extractData(ResultSet rs) throws SQLException,
				DataAccessException {
			List<T> result = new ArrayList<T>();
			int rowNum = 0;
			int end = start + len;
			while (rs.next()) {
				rowNum++;
				if (rowNum <= start) {
					continue;
				} else if (rowNum > end) {
					break;
				} else {
					result.add(this.rowMapper.mapRow(rs, rowNum));
				}
			}
			return result;
		}
	}

	private int countRecordsNumberByExample(JdbcPageInfo pageInfo) {
		String sql = "select count(0) from (" + pageInfo.getSql() + ") source_query_alias";
		log.debug("===========count SQL:" + sql);
		return namedParameterJdbcTemplate.queryForInt(sql,
				new MapSqlParameterSource(pageInfo.getExampleModel()));
	}

}

package org.mxhero.console.backend.security;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.console.backend.infrastructure.pagination.common.PageResult;
import org.mxhero.console.backend.infrastructure.pagination.jdbc.BaseJdbcDao;
import org.mxhero.console.backend.infrastructure.pagination.jdbc.JdbcPageInfo;
import org.mxhero.console.backend.repository.jdbc.mapper.UserMapper;
import org.mxhero.console.backend.vo.ApplicationUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/app-config-test.xml"})
public class PagingQueryTest {

	private NamedParameterJdbcTemplate template;
	private String SELECT = " SELECT" +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.ID+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.CREATION+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.LAST_NAME+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.LOCALE+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.NAME+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.NOTIFY_MAIL+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.SOUNDS_ENABLED+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.USER_NAME+"`" +
			" FROM `"+UserMapper.DATABASE+"`.`"+UserMapper.TABLE_NAME+"`";
	
	@Autowired
	public void setTemplate(@Qualifier("mxheroDataSource")DataSource ds) {
		this.template = new NamedParameterJdbcTemplate(ds);
	}
	
	public NamedParameterJdbcTemplate getTemplate(){
		return template;
	}
	
	@Test
	public void test(){
		BaseJdbcDao<ApplicationUserVO> jdbc = new BaseJdbcDao<ApplicationUserVO>();
		jdbc.setNamedParameterJdbcTemplate(getTemplate());
		JdbcPageInfo page = new JdbcPageInfo();
		page.putSql(SELECT);
		page.putRowMapper(new UserMapper());
		PageResult<ApplicationUserVO> result = jdbc.findByPage(page);
	}

	
}

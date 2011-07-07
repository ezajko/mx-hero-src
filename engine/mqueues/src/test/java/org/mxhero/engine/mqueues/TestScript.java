package org.mxhero.engine.mqueues;

import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/script-test-bundle-context.xml" })
public class TestScript {

	@Autowired
	private JdbcConnectionPool pool;

	@Test
	public void test() throws SQLException{
		pool.getConnection().createStatement().execute("SCRIPT NODATA DROP TO 'C:\\temp\\create.sql'");
	}
	
	public JdbcConnectionPool getPool() {
		return pool;
	}

	public void setPool(JdbcConnectionPool pool) {
		this.pool = pool;
	}
	
}

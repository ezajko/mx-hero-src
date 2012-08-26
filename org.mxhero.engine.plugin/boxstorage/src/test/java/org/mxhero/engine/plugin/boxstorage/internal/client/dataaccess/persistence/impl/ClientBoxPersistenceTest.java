package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.persistence.impl;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.persistence.impl.ClientBoxPersistence;
import org.mxhero.engine.plugin.boxstorage.internal.client.service.UserBoxClient;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class ClientBoxPersistenceTest {

	ClientBoxPersistence target;
	@Mock
	private DataSource ds;
	@Mock
	private NamedParameterJdbcTemplate jdbc;
	
	@Before
	public void setUp() throws Exception {
		target = new ClientBoxPersistence(ds);
		target.setJdbc(jdbc);
	}

	@Test
	public void test_store_token_insert() {
		UserBoxClient userBoxClient = new UserBoxClient();
		when(jdbc.query(anyString(), anyMap(), any(RowMapper.class))).thenReturn(Lists.newArrayList());
		target.storeToken(userBoxClient);
		verify(jdbc,times(1)).update(anyString(), any(SqlParameterSource.class));
	}

	@Test
	public void test_store_token_update() {
		UserBoxClient userBoxClient = new UserBoxClient();
		when(jdbc.query(anyString(), anyMap(), any(RowMapper.class))).thenReturn(Lists.newArrayList(1));
		target.storeToken(userBoxClient);
		verify(jdbc,times(1)).update(anyString(), any(SqlParameterSource.class));
	}
}

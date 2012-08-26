package org.mxhero.engine.plugin.boxserver.dataaccess.persistence;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mxhero.engine.plugin.boxserver.dataaccess.persistence.impl.BoxStoragePersistence;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.CreateUserResponse;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.Item;
import org.mxhero.engine.plugin.boxserver.service.UserBox;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class BoxStoragePersistenceTest {
	
	BoxStoragePersistence target;
	
	@Mock
	DataSource ds;
	
	@Mock
	NamedParameterJdbcTemplate jdbc;

	@Before
	public void setUp() throws Exception {
		target = new BoxStoragePersistence(ds);
		target.setJdbc(jdbc);
	}

	@Test
	public void testStoreAuthToken() {
		when(jdbc.query(anyString(), anyMap(), any(RowMapper.class))).thenReturn(Lists.newArrayList());
		target.storeAuthToken(new UserBox());
		verify(jdbc, times(1)).query(anyString(), anyMap(), any(RowMapper.class));
		verify(jdbc, times(1)).update(anyString(), any(BeanPropertySqlParameterSource.class));
	}

	@Test
	public void testStoreAuthToken_for_update() {
		when(jdbc.query(anyString(), anyMap(), any(RowMapper.class))).thenReturn(Lists.newArrayList(new CreateUserResponse()));
		target.storeAuthToken(new UserBox());
		verify(jdbc, times(1)).query(anyString(), anyMap(), any(RowMapper.class));
		verify(jdbc, times(1)).update(anyString(), any(BeanPropertySqlParameterSource.class));
	}

	@Test
	public void testGetAccountFromStorage() {
		CreateUserResponse user = new CreateUserResponse();
		user.setToken("AAAA");
		user.setItem(new Item());
		user.getItem().setId("1");
		when(jdbc.query(anyString(), anyMap(), any(RowMapper.class))).thenReturn(Lists.newArrayList(user));
		CreateUserResponse accountFromStorage = target.getAccountFromStorage("pepe@pepe.com");
		assertNotNull(accountFromStorage);
		assertEquals("AAAA", accountFromStorage.getToken());
		assertEquals("1", accountFromStorage.getItem().getId());
		verify(jdbc, never()).update(anyString(), any(BeanPropertySqlParameterSource.class));
	}

}

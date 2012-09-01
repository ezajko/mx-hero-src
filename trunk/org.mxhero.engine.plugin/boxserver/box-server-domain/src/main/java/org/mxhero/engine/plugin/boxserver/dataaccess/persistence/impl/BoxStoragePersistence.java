package org.mxhero.engine.plugin.boxserver.dataaccess.persistence.impl;

import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.mxhero.engine.plugin.boxserver.Application;
import org.mxhero.engine.plugin.boxserver.dataaccess.persistence.StoragePersistence;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.CreateUserResponse;
import org.mxhero.engine.plugin.boxserver.service.UserBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * The Class BoxStoragePersistence.
 */
public class BoxStoragePersistence implements StoragePersistence {

	/** The jdbc. */
	private NamedParameterJdbcTemplate jdbc;

	/** The logger. */
	private static Logger logger = LoggerFactory
			.getLogger(BoxStoragePersistence.class);

	/**
	 * Instantiates a new box storage persistence.
	 * 
	 * @param dataSource
	 *            the data source
	 */
	public BoxStoragePersistence(DataSource dataSource) {
		jdbc = new NamedParameterJdbcTemplate(dataSource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mxhero.engine.plugin.boxserver.internal
	 * .
	 * box.dataaccess.persistence.impl.StoragePersistence#storeAuthToken(org.mxhero
	 * .engine.plugin.attachmentlink.cloudstorage.internal.box.internal.box.
	 * dataaccess.rest.box.CreateUserResponse)
	 */
	@Override
	public void storeAuthToken(UserBox response) {
		logger.debug("Storaging user token for user {}", response);
		CreateUserResponse storage = getAccountFromStorage(response.getEmail());
		SqlParameterSource paramMap = new BeanPropertySqlParameterSource(
				response);
		if (storage == null) {
			logger.debug("Registering new user token for user {}", response);
			jdbc.update(
					"INSERT INTO attachments_box_service.user_box_token (email,token,created_by_us,application_id) values(:email,:account.token,:createdByUs,:applicationId)",
					paramMap);
		} else {
			logger.debug(
					"User already have been registered. Updating user token for {}",
					response);
			jdbc.update(
					"UPDATE attachments_box_service.user_box_token set token = :account.token where email = :email",
					paramMap);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mxhero.engine.plugin.boxserver.internal
	 * .
	 * box.dataaccess.persistence.StoragePersistence#getAccountFromStorage(java.
	 * lang.String)
	 */
	@Override
	public CreateUserResponse getAccountFromStorage(String email) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("email", email);
		List<CreateUserResponse> query = jdbc.query(
				"SELECT token from attachments_box_service.user_box_token where email = :email",
				params, new BeanPropertyRowMapper<CreateUserResponse>(
						CreateUserResponse.class));
		if (query.isEmpty())
			return null;
		return query.get(0);
	}

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.boxserver.dataaccess.persistence.StoragePersistence#getAppFromKey(java.lang.String)
	 */
	@Override
	public Application getAppFromKey(String appKey) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("name", appKey);
		List<Application> query = jdbc
				.query("SELECT name, enabled from attachments_box_service.applications where name = :name",
						params, new BeanPropertyRowMapper<Application>(
								Application.class));
		if (query.isEmpty()) {
			return new Application();
		}
		return query.get(0);
	}
	
	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.boxserver.dataaccess.persistence.StoragePersistence#registerNewApp(java.lang.String)
	 */
	@Override
	public void registerNewApp(String name) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		jdbc.update("INSERT INTO attachments_box_service.applications (name, enabled) values(:name, true)", params);
	}
	
	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.boxserver.dataaccess.persistence.StoragePersistence#authenticateModule(java.lang.String)
	 */
	@Override
	public boolean authenticateModule(String moduleName) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("name", moduleName);
		List<Boolean> query = jdbc
				.query("SELECT enabled from attachments_box_service.storage_version where storage_name = :name",
						params, new SingleColumnRowMapper<Boolean>(Boolean.class));
		if (query.isEmpty()) {
			return false;
		}
		return query.get(0);
	}

	/**
	 * Sets the jdbc.
	 * 
	 * @param jdbc
	 *            the new jdbc
	 */
	public void setJdbc(NamedParameterJdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

}

package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.persistence.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.persistence.ClientStoragePersistence;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.Entry;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response.CreateTokenResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.service.UserBoxClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * The Class ClientBoxPersistence.
 */
public class ClientBoxPersistence implements ClientStoragePersistence {

	/** The jdbc. */
	private NamedParameterJdbcTemplate jdbc;

	/** The logger. */
	private static Logger logger = LoggerFactory
			.getLogger(ClientBoxPersistence.class);

	/**
	 * Instantiates a new box storage persistence.
	 * 
	 * @param dataSource
	 *            the data source
	 */
	public ClientBoxPersistence(DataSource dataSource) {
		jdbc = new NamedParameterJdbcTemplate(dataSource);
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


	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.persistence.ClientStoragePersistence#getAccountFromStorage(java.lang.String)
	 */
	@Override
	public CreateTokenResponse getAccountFromStorage(String email) {
		SqlParameterSource params = new MapSqlParameterSource("email", email);
		List<CreateTokenResponse> query = jdbc.query("SELECT email, token from attachments_box_client.user_box_token where email = :email", params, new BeanPropertyRowMapper<CreateTokenResponse>(CreateTokenResponse.class));
		if(query.isEmpty())return null;
		return query.get(0);
	}


	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.persistence.ClientStoragePersistence#storeToken(org.mxhero.engine.plugin.boxstorage.internal.client.service.UserBoxClient)
	 */
	@Override
	public void storeToken(UserBoxClient userBoxClient) {
		logger.debug("Store token for client {}", userBoxClient);
		SqlParameterSource params = new BeanPropertySqlParameterSource(userBoxClient);
		RowMapper<Integer> mapper = new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("exist");
			}
		};
		List<Integer> queryForInt = jdbc.query("SELECT 1 as exist from attachments_box_client.user_box_token where email = :email", params, mapper);
		if(queryForInt.isEmpty()){
			logger.debug("Insert new token");
			jdbc.update("INSERT INTO attachments_box_client.user_box_token (email, token) values(:email, :account.token)", params);
		}else{
			logger.debug("Update previous token");
			jdbc.update("UPDATE attachments_box_client.user_box_token set token = :account.token where email = :email", params);
		} 
	}


	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.persistence.ClientStoragePersistence#registerStoreFile(org.mxhero.engine.plugin.boxstorage.internal.client.service.UserBoxClient, java.lang.String)
	 */
	@Override
	public void registerStoreFile(UserBoxClient userBoxClient, String filePath) {
		logger.debug("Register file {} in local storage {}", filePath,  userBoxClient);
		MapSqlParameterSource params = new MapSqlParameterSource("filePath", filePath);
		params.addValue("email", userBoxClient.getEmail());
		Entry entry = userBoxClient.getFileStored().getEntries().get(0);
		if(userBoxClient.getFileStored().couldCreateSharedLink()){
			params.addValue("url", entry.getShared_link().getUrl());
		}else{
			params.addValue("url", null);
		}
		params.addValue("idBox", entry.getId());
		jdbc.update("INSERT INTO attachments_box_client.user_box_file (file_path, email, public_url, id_file_box) values(:filePath, :email, :url, :idBox)", params);
	}
	
	@Override
	public boolean hasBeenProccessed(String filePath) {
		SqlParameterSource params = new MapSqlParameterSource("file", filePath);
		RowMapper<Integer> mapper = new SingleColumnRowMapper<Integer>(Integer.class);
		List<Integer> queryForInt = jdbc.query("SELECT 1 as exist from attachments_box_client.user_box_file where file_path = :file", params, mapper);
		return !queryForInt.isEmpty();
	}

}

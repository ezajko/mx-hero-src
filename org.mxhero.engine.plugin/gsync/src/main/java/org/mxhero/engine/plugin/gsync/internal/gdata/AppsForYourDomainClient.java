/* Copyright (c) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mxhero.engine.plugin.gsync.internal.gdata;

import com.google.gdata.client.appsforyourdomain.AppsForYourDomainQuery;
import com.google.gdata.client.appsforyourdomain.AppsGroupsService;
import com.google.gdata.client.appsforyourdomain.NicknameService;
import com.google.gdata.client.appsforyourdomain.UserService;
import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.data.Link;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.Login;
import com.google.gdata.data.appsforyourdomain.Name;
import com.google.gdata.data.appsforyourdomain.Nickname;
import com.google.gdata.data.appsforyourdomain.Quota;
import com.google.gdata.data.appsforyourdomain.provisioning.NicknameEntry;
import com.google.gdata.data.appsforyourdomain.provisioning.NicknameFeed;
import com.google.gdata.data.appsforyourdomain.provisioning.UserEntry;
import com.google.gdata.data.appsforyourdomain.provisioning.UserFeed;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the client library for the Google Apps for Your Domain GData API. It
 * shows how the AppsForYourDomainService can be used to manage users on a
 * domain. This class contains a number of methods which can be used to create,
 * update, retrieve, and delete entities such as users, email lists and
 * nicknames. Also included is sample usage of the client library.
 * 
 */
public class AppsForYourDomainClient {

	private static final Logger LOGGER = Logger
			.getLogger(AppsForYourDomainClient.class.getName());

	private static final String APPS_FEEDS_URL_BASE = "https://apps-apis.google.com/a/feeds/";

	protected static final String SERVICE_VERSION = "2.0";

	protected String domainUrlBase;

	protected NicknameService nicknameService;
	protected UserService userService;
	protected AppsGroupsService groupService;

	/**
	 * Public getter for AppsGroupsService
	 * 
	 * @return the groupService
	 */
	public AppsGroupsService getGroupService() {
		return groupService;
	}

	protected final String domain;

	protected AppsForYourDomainClient(String domain) {
		this.domain = domain;
		this.domainUrlBase = APPS_FEEDS_URL_BASE + domain + "/";
	}

	/**
	 * Constructs an AppsForYourDomainClient for the given domain using the
	 * given admin credentials.
	 * 
	 * @param adminEmail
	 *            An admin user's email address such as admin@domain.com
	 * @param adminPassword
	 *            The admin's password
	 * @param domain
	 *            The domain to administer
	 */
	public AppsForYourDomainClient(GoogleOAuthParameters parameters,
			String domain) throws Exception {
		this(domain);

		// Configure all of the different Provisioning services
		userService = new UserService(
				"org.mxhero.engine.plugin.gsync-UserService");
		userService.setOAuthCredentials(parameters, new OAuthHmacSha1Signer());

		nicknameService = new NicknameService(
				"org.mxhero.engine.plugin.gsync-NicknameService");
		nicknameService.setOAuthCredentials(parameters, new OAuthHmacSha1Signer());

		groupService = new AppsGroupsService(domain,
				"org.mxhero.engine.plugin.gsync-AppsGroupService");
		groupService.setOAuthCredentials(parameters, new OAuthHmacSha1Signer());
	}


	/**
	 * Creates a new user with an email account.
	 * 
	 * @param username
	 *            The username of the new user.
	 * @param givenName
	 *            The given name for the new user.
	 * @param familyName
	 *            the family name for the new user.
	 * @param password
	 *            The password for the new user.
	 * @return A UserEntry object of the newly created user.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserEntry createUser(String username, String givenName,
			String familyName, String password)
			throws AppsForYourDomainException, ServiceException, IOException {

		return createUser(username, givenName, familyName, password, null, null);
	}

	/**
	 * Creates a new user with an email account.
	 * 
	 * @param username
	 *            The username of the new user.
	 * @param givenName
	 *            The given name for the new user.
	 * @param familyName
	 *            the family name for the new user.
	 * @param password
	 *            The password for the new user.
	 * @param quotaLimitInMb
	 *            User's quota limit in megabytes. This field is only used for
	 *            domains with custom quota limits.
	 * @return A UserEntry object of the newly created user.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserEntry createUser(String username, String givenName,
			String familyName, String password, Integer quotaLimitInMb)
			throws AppsForYourDomainException, ServiceException, IOException {

		return createUser(username, givenName, familyName, password, null,
				quotaLimitInMb);
	}

	/**
	 * Creates a new user with an email account.
	 * 
	 * @param username
	 *            The username of the new user.
	 * @param givenName
	 *            The given name for the new user.
	 * @param familyName
	 *            the family name for the new user.
	 * @param password
	 *            The password for the new user.
	 * @param passwordHashFunction
	 *            The name of the hash function to hash the password
	 * @return A UserEntry object of the newly created user.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserEntry createUser(String username, String givenName,
			String familyName, String password, String passwordHashFunction)
			throws AppsForYourDomainException, ServiceException, IOException {

		return createUser(username, givenName, familyName, password,
				passwordHashFunction, null);
	}

	/**
	 * Creates a new user with an email account.
	 * 
	 * @param username
	 *            The username of the new user.
	 * @param givenName
	 *            The given name for the new user.
	 * @param familyName
	 *            the family name for the new user.
	 * @param password
	 *            The password for the new user.
	 * @param passwordHashFunction
	 *            Specifies the hash format of the password parameter
	 * @param quotaLimitInMb
	 *            User's quota limit in megabytes. This field is only used for
	 *            domains with custom quota limits.
	 * @return A UserEntry object of the newly created user.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserEntry createUser(String username, String givenName,
			String familyName, String password, String passwordHashFunction,
			Integer quotaLimitInMb) throws AppsForYourDomainException,
			ServiceException, IOException {

		LOGGER.log(Level.INFO, "Creating user '"
				+ username
				+ "'. Given Name: '"
				+ givenName
				+ "' Family Name: '"
				+ familyName
				+ (passwordHashFunction != null ? "' Hash Function: '"
						+ passwordHashFunction : "")
				+ (quotaLimitInMb != null ? "' Quota Limit: '" + quotaLimitInMb
						+ "'." : "'."));

		UserEntry entry = new UserEntry();
		Login login = new Login();
		login.setUserName(username);
		login.setPassword(password);
		if (passwordHashFunction != null) {
			login.setHashFunctionName(passwordHashFunction);
		}
		entry.addExtension(login);

		Name name = new Name();
		name.setGivenName(givenName);
		name.setFamilyName(familyName);
		entry.addExtension(name);

		if (quotaLimitInMb != null) {
			Quota quota = new Quota();
			quota.setLimit(quotaLimitInMb);
			entry.addExtension(quota);
		}

		URL insertUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION);
		return userService.insert(insertUrl, entry);
	}

	/**
	 * Retrieves a user.
	 * 
	 * @param username
	 *            The user you wish to retrieve.
	 * @return A UserEntry object of the retrieved user.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserEntry retrieveUser(String username)
			throws AppsForYourDomainException, ServiceException, IOException {

		LOGGER.log(Level.INFO, "Retrieving user '" + username + "'.");

		URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION
				+ "/" + username);
		return userService.getEntry(retrieveUrl, UserEntry.class);
	}

	/**
	 * Retrieves all users in domain. This method may be very slow for domains
	 * with a large number of users. Any changes to users, including creations
	 * and deletions, which are made after this method is called may or may not
	 * be included in the Feed which is returned.
	 * 
	 * @return A UserFeed object of the retrieved users.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserFeed retrieveAllUsers() throws AppsForYourDomainException,
			ServiceException, IOException {

		LOGGER.log(Level.INFO, "Retrieving all users.");

		URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION
				+ "/");
		UserFeed allUsers = new UserFeed();
		UserFeed currentPage;
		Link nextLink;

		do {
			currentPage = userService.getFeed(retrieveUrl, UserFeed.class);
			allUsers.getEntries().addAll(currentPage.getEntries());
			nextLink = currentPage.getLink(Link.Rel.NEXT, Link.Type.ATOM);
			if (nextLink != null) {
				retrieveUrl = new URL(nextLink.getHref());
			}
		} while (nextLink != null);

		return allUsers;
	}

	/**
	 * Retrieves one page (100) of users in domain. Any changes to users,
	 * including creations and deletions, which are made after this method is
	 * called may or may not be included in the Feed which is returned. If the
	 * optional startUsername parameter is specified, one page of users is
	 * returned which have usernames at or after the startUsername as per ASCII
	 * value ordering with case-insensitivity. A value of null or empty string
	 * indicates you want results from the beginning of the list.
	 * 
	 * @param startUsername
	 *            The starting point of the page (optional).
	 * @return A UserFeed object of the retrieved users.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserFeed retrievePageOfUsers(String startUsername)
			throws AppsForYourDomainException, ServiceException, IOException {

		LOGGER.log(Level.INFO,
				"Retrieving one page of users"
						+ (startUsername != null ? " starting at "
								+ startUsername : "") + ".");

		URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION
				+ "/");
		AppsForYourDomainQuery query = new AppsForYourDomainQuery(retrieveUrl);
		query.setStartUsername(startUsername);
		return userService.query(query, UserFeed.class);
	}

	/**
	 * Updates a user.
	 * 
	 * @param username
	 *            The user to update.
	 * @param userEntry
	 *            The updated UserEntry for the user.
	 * @return A UserEntry object of the newly updated user.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserEntry updateUser(String username, UserEntry userEntry)
			throws AppsForYourDomainException, ServiceException, IOException {

		LOGGER.log(Level.INFO, "Updating user '" + username + "'.");

		URL updateUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/"
				+ username);
		return userService.update(updateUrl, userEntry);
	}

	/**
	 * Deletes a user.
	 * 
	 * @param username
	 *            The user you wish to delete.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public void deleteUser(String username) throws AppsForYourDomainException,
			ServiceException, IOException {

		LOGGER.log(Level.INFO, "Deleting user '" + username + "'.");

		URL deleteUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/"
				+ username);
		userService.delete(deleteUrl);
	}

	/**
	 * Suspends a user. Note that executing this method for a user who is
	 * already suspended has no effect.
	 * 
	 * @param username
	 *            The user you wish to suspend.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserEntry suspendUser(String username)
			throws AppsForYourDomainException, ServiceException, IOException {

		LOGGER.log(Level.INFO, "Suspending user '" + username + "'.");

		URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION
				+ "/" + username);
		UserEntry userEntry = userService
				.getEntry(retrieveUrl, UserEntry.class);
		userEntry.getLogin().setSuspended(true);

		URL updateUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/"
				+ username);
		return userService.update(updateUrl, userEntry);
	}

	/**
	 * Restores a user. Note that executing this method for a user who is not
	 * suspended has no effect.
	 * 
	 * @param username
	 *            The user you wish to restore.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserEntry restoreUser(String username)
			throws AppsForYourDomainException, ServiceException, IOException {

		LOGGER.log(Level.INFO, "Restoring user '" + username + "'.");

		URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION
				+ "/" + username);
		UserEntry userEntry = userService
				.getEntry(retrieveUrl, UserEntry.class);
		userEntry.getLogin().setSuspended(false);

		URL updateUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/"
				+ username);
		return userService.update(updateUrl, userEntry);
	}

	/**
	 * Set admin privilege for user. Note that executing this method for a user
	 * who is already an admin has no effect.
	 * 
	 * @param username
	 *            The user you wish to make an admin.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific error occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserEntry addAdminPrivilege(String username)
			throws AppsForYourDomainException, ServiceException, IOException {

		LOGGER.log(Level.INFO, "Setting admin privileges for user '" + username
				+ "'.");

		URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION
				+ "/" + username);
		UserEntry userEntry = userService
				.getEntry(retrieveUrl, UserEntry.class);
		userEntry.getLogin().setAdmin(true);

		URL updateUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/"
				+ username);
		return userService.update(updateUrl, userEntry);
	}

	/**
	 * Remove admin privilege for user. Note that executing this method for a
	 * user who is not an admin has no effect.
	 * 
	 * @param username
	 *            The user you wish to remove admin privileges.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific error occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserEntry removeAdminPrivilege(String username)
			throws AppsForYourDomainException, ServiceException, IOException {

		LOGGER.log(Level.INFO, "Removing admin privileges for user '"
				+ username + "'.");

		URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION
				+ "/" + username);
		UserEntry userEntry = userService
				.getEntry(retrieveUrl, UserEntry.class);
		userEntry.getLogin().setAdmin(false);

		URL updateUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/"
				+ username);
		return userService.update(updateUrl, userEntry);
	}

	/**
	 * Require a user to change password at next login. Note that executing this
	 * method for a user who is already required to change password at next
	 * login as no effect.
	 * 
	 * @param username
	 *            The user who must change his or her password.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public UserEntry forceUserToChangePassword(String username)
			throws AppsForYourDomainException, ServiceException, IOException {

		LOGGER.log(Level.INFO, "Requiring " + username
				+ " to change password at " + "next login.");

		URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION
				+ "/" + username);
		UserEntry userEntry = userService
				.getEntry(retrieveUrl, UserEntry.class);
		userEntry.getLogin().setChangePasswordAtNextLogin(true);

		URL updateUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/"
				+ username);
		return userService.update(updateUrl, userEntry);
	}

	/**
	 * Creates a nickname for the username.
	 * 
	 * @param username
	 *            The user for which we want to create a nickname.
	 * @param nickname
	 *            The nickname you wish to create.
	 * @return A NicknameEntry object of the newly created nickname.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public NicknameEntry createNickname(String username, String nickname)
			throws AppsForYourDomainException, ServiceException, IOException {

		LOGGER.log(Level.INFO, "Creating nickname '" + nickname
				+ "' for user '" + username + "'.");

		NicknameEntry entry = new NicknameEntry();
		Nickname nicknameExtension = new Nickname();
		nicknameExtension.setName(nickname);
		entry.addExtension(nicknameExtension);

		Login login = new Login();
		login.setUserName(username);
		entry.addExtension(login);

		URL insertUrl = new URL(domainUrlBase + "nickname/" + SERVICE_VERSION);
		return nicknameService.insert(insertUrl, entry);
	}

	/**
	 * Retrieves a nickname.
	 * 
	 * @param nickname
	 *            The nickname you wish to retrieve.
	 * @return A NicknameEntry object of the newly created nickname.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public NicknameEntry retrieveNickname(String nickname)
			throws AppsForYourDomainException, ServiceException, IOException {
		LOGGER.log(Level.INFO, "Retrieving nickname '" + nickname + "'.");

		URL retrieveUrl = new URL(domainUrlBase + "nickname/" + SERVICE_VERSION
				+ "/" + nickname);
		return nicknameService.getEntry(retrieveUrl, NicknameEntry.class);
	}

	/**
	 * Retrieves all nicknames for the given username.
	 * 
	 * @param username
	 *            The user for which you want all nicknames.
	 * @return A NicknameFeed object with all the nicknames for the user.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public NicknameFeed retrieveNicknames(String username)
			throws AppsForYourDomainException, ServiceException, IOException {
		LOGGER.log(Level.INFO, "Retrieving nicknames for user '" + username
				+ "'.");

		URL feedUrl = new URL(domainUrlBase + "nickname/" + SERVICE_VERSION);
		AppsForYourDomainQuery query = new AppsForYourDomainQuery(feedUrl);
		query.setUsername(username);
		return nicknameService.query(query, NicknameFeed.class);
	}

	/**
	 * Retrieves one page (100) of nicknames in domain. Any changes to
	 * nicknames, including creations and deletions, which are made after this
	 * method is called may or may not be included in the Feed which is
	 * returned. If the optional startNickname parameter is specified, one page
	 * of nicknames is returned which have names at or after startNickname as
	 * per ASCII value ordering with case-insensitivity. A value of null or
	 * empty string indicates you want results from the beginning of the list.
	 * 
	 * @param startNickname
	 *            The starting point of the page (optional).
	 * @return A NicknameFeed object of the retrieved nicknames.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public NicknameFeed retrievePageOfNicknames(String startNickname)
			throws AppsForYourDomainException, ServiceException, IOException {

		LOGGER.log(Level.INFO,
				"Retrieving one page of nicknames"
						+ (startNickname != null ? " starting at "
								+ startNickname : "") + ".");

		URL retrieveUrl = new URL(domainUrlBase + "nickname/" + SERVICE_VERSION
				+ "/");
		AppsForYourDomainQuery query = new AppsForYourDomainQuery(retrieveUrl);
		query.setStartNickname(startNickname);
		return nicknameService.query(query, NicknameFeed.class);
	}

	/**
	 * Retrieves all nicknames in domain. This method may be very slow for
	 * domains with a large number of nicknames. Any changes to nicknames,
	 * including creations and deletions, which are made after this method is
	 * called may or may not be included in the Feed which is returned.
	 * 
	 * @return A NicknameFeed object of the retrieved nicknames.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public NicknameFeed retrieveAllNicknames()
			throws AppsForYourDomainException, ServiceException, IOException {

		LOGGER.log(Level.INFO, "Retrieving all nicknames.");

		URL retrieveUrl = new URL(domainUrlBase + "nickname/" + SERVICE_VERSION
				+ "/");
		NicknameFeed allNicknames = new NicknameFeed();
		NicknameFeed currentPage;
		Link nextLink;

		do {
			currentPage = nicknameService.getFeed(retrieveUrl,
					NicknameFeed.class);
			allNicknames.getEntries().addAll(currentPage.getEntries());
			nextLink = currentPage.getLink(Link.Rel.NEXT, Link.Type.ATOM);
			if (nextLink != null) {
				retrieveUrl = new URL(nextLink.getHref());
			}
		} while (nextLink != null);

		return allNicknames;
	}

	/**
	 * Deletes a nickname.
	 * 
	 * @param nickname
	 *            The nickname you wish to delete.
	 * @throws AppsForYourDomainException
	 *             If a Provisioning API specific occurs.
	 * @throws ServiceException
	 *             If a generic GData framework error occurs.
	 * @throws IOException
	 *             If an error occurs communicating with the GData service.
	 */
	public void deleteNickname(String nickname)
			throws AppsForYourDomainException, ServiceException, IOException {

		LOGGER.log(Level.INFO, "Deleting nickname '" + nickname + "'.");

		URL deleteUrl = new URL(domainUrlBase + "nickname/" + SERVICE_VERSION
				+ "/" + nickname);
		nicknameService.delete(deleteUrl);
	}
	
}
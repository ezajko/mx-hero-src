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
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.Link;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.provisioning.NicknameEntry;
import com.google.gdata.data.appsforyourdomain.provisioning.NicknameFeed;
import com.google.gdata.data.appsforyourdomain.provisioning.UserEntry;
import com.google.gdata.data.appsforyourdomain.provisioning.UserFeed;
import com.google.gdata.data.contacts.ContactFeed;
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
	protected ContactsService contactService;

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
		
		contactService = new ContactsService("org.mxhero.engine.plugin.gsync-ContactService");
		contactService.setOAuthCredentials(parameters, new OAuthHmacSha1Signer());
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
	public ContactFeed retrieveAllContacts() throws AppsForYourDomainException,
			ServiceException, IOException {

		LOGGER.log(Level.INFO, "Retrieving all users.");

		URL retrieveUrl = new URL(
				"https://www.google.com/m8/feeds/profiles/domain/"+domain+"/full"+"?xoauth_requestor_id=bruno@mxhero.com");
		ContactFeed allContacts = new ContactFeed();
		ContactFeed currentPage;
		Link nextLink;

		do {
			currentPage = contactService.getFeed(retrieveUrl, ContactFeed.class);
			allContacts.getEntries().addAll(currentPage.getEntries());
			nextLink = currentPage.getLink(Link.Rel.NEXT, Link.Type.ATOM);
			if (nextLink != null) {
				retrieveUrl = new URL(nextLink.getHref());
			}
		} while (nextLink != null);

		return allContacts;
	}
	
}
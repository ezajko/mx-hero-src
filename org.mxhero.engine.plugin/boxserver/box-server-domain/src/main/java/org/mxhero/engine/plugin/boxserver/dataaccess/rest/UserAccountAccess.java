package org.mxhero.engine.plugin.boxserver.dataaccess.rest;

import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.CreateUserResponse;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.Item;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.ItemResponse;
import org.mxhero.engine.plugin.boxserver.service.UserBox;

// TODO: Auto-generated Javadoc
/**
 * The Interface UserAccountAccess.
 */
public interface UserAccountAccess {
	
	/**
	 * Creates the account.
	 *
	 * @param email the email
	 * @return the creates the user response
	 */
	public CreateUserResponse createAccount(String email);
	
	/**
	 * Gets the folder mx hero.
	 *
	 * @param userBox the user box
	 * @return the folder mx hero
	 */
	public ItemResponse getFolderMxHero(UserBox userBox);

	/**
	 * Creates the mx hero folder.
	 *
	 * @param userBox the user box
	 * @return the item
	 */
	public Item createMxHeroFolder(UserBox userBox);

}

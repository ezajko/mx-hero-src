package org.mxhero.engine.plugin.boxstorage.internal.client.service.strategy.impl;

import org.mxhero.engine.plugin.boxstorage.internal.client.service.strategy.AlreadyAccountStrategy;
import org.mxhero.engine.plugin.storageapi.UserResult;
import org.mxhero.engine.plugin.storageapi.UserResultMessage;

/**
 * The Class RecipientAlreadyAccountStrategy.
 */
public class RecipientAlreadyAccountStrategy implements AlreadyAccountStrategy {

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.boxstorage.internal.client.service.strategy.AlreadyAccountStrategy#execute(org.mxhero.engine.plugin.storageapi.UserResult)
	 */
	@Override
	public void execute(UserResult result) {
		result.setMessage(UserResultMessage.SEND_HTML_WITH_LINK);
	}

}

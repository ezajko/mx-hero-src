package org.mxhero.engine.plugin.boxstorage.internal.client.service.strategy;

import org.mxhero.engine.plugin.boxstorage.internal.client.domain.UserRequest;
import org.mxhero.engine.plugin.boxstorage.internal.client.service.strategy.impl.RecipientAlreadyAccountStrategy;
import org.mxhero.engine.plugin.boxstorage.internal.client.service.strategy.impl.SenderAlreadyAccountStrategy;

/**
 * A factory for creating AlreadyAccountStrategy objects.
 */
public class AlreadyAccountStrategyFactory {

	/**
	 * Creates the.
	 *
	 * @param request the request
	 * @return the already account strategy
	 */
	public static AlreadyAccountStrategy create(UserRequest request) {
		if(request.isSender()){
			return new SenderAlreadyAccountStrategy();
		}else if(request.isRecipient()){
			return new RecipientAlreadyAccountStrategy();
		}
		throw new RuntimeException("We couldn't identify if the user is sender or recipient. "+request);
	}

}

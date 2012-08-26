package org.mxhero.engine.plugin.attachmentlink.alcommand.external;

import java.util.List;

/**
 * The Interface AttachmentService.
 */
public interface AttachmentService {

	/**
	 * Gets the transaction to process.
	 *
	 * @param limit the limit
	 * @return the transaction to process
	 */
	List<TransactionAttachment> getTransactionToProcess(Integer limit);

	/**
	 * Send message.
	 *
	 * @param tx the tx
	 * @param b the b
	 */
	void sendMessage(TransactionAttachment tx, boolean b);

}

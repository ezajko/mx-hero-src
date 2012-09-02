package org.mxhero.engine.plugin.boxstorage.internal.sinchro;

import org.mxhero.engine.plugin.attachmentlink.alcommand.service.TransactionAttachment;
import org.mxhero.engine.plugin.boxstorage.internal.client.BoxCloudStorageClient;
import org.mxhero.engine.plugin.boxstorage.internal.client.StorageResult;

public class CloudStorageMock extends BoxCloudStorageClient {

	@Override
	public StorageResult store(TransactionAttachment tx) {
		return new StorageResult(true);
	}

}

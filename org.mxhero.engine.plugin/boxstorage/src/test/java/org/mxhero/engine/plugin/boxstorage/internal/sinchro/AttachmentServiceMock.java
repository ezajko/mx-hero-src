package org.mxhero.engine.plugin.boxstorage.internal.sinchro;

import java.util.ArrayList;
import java.util.List;

import org.mxhero.engine.plugin.attachmentlink.alcommand.service.AttachmentService;
import org.mxhero.engine.plugin.attachmentlink.alcommand.service.TransactionAttachment;

public class AttachmentServiceMock implements AttachmentService{

	@Override
	public List<TransactionAttachment> getTransactionToProcess(Integer count) {
		List<TransactionAttachment> txs = new ArrayList<TransactionAttachment>();
		for (int i = 0; i < count; i++) {
			TransactionAttachment e = new TransactionAttachment();
			e.setEmail("email"+i);
			e.setFilePath("filepath"+i);
			e.setIdMessageAttach(new Long(i));
			txs.add(e);
		}

		return txs;
	}

	@Override
	public void sendMessage(TransactionAttachment arg0, boolean arg1) {
		System.out.printf("Transaction proccessed %s with result %s %n",arg0, arg1);
	}

}

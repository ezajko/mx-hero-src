package org.mxhero.engine.plugin.boxstorage.internal.sinchro;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.mxhero.engine.plugin.attachmentlink.alcommand.service.AttachmentService;
import org.mxhero.engine.plugin.attachmentlink.alcommand.service.TransactionAttachment;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * The Class ProducerQueue.
 */
public class ProducerQueue implements Runnable,BeanFactoryAware {

	/** The queue. */
	private final BlockingQueue<TransactionAttachment> queue;
	
	/** The bean. */
	private BeanFactory bean;

	/**
	 * Instantiates a new producer queue.
	 *
	 * @param queue the queue
	 */
	public ProducerQueue(BlockingQueue<TransactionAttachment> queue) {
		this.queue = queue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		List<TransactionAttachment> transactions = getService().getTransactionToProcess(100);
		this.queue.addAll(transactions);
	}
	
	/**
	 * Gets the service.
	 *
	 * @return the service
	 */
	private AttachmentService getService(){
		return (AttachmentService) this.bean.getBean("attachmentLinkExternalService");
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	@Override
	public void setBeanFactory(BeanFactory bean) throws BeansException {
		this.bean = bean;
	}

}

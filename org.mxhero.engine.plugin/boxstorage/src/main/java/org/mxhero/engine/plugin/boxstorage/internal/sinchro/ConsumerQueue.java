package org.mxhero.engine.plugin.boxstorage.internal.sinchro;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.mxhero.engine.plugin.attachmentlink.alcommand.service.AttachmentService;
import org.mxhero.engine.plugin.attachmentlink.alcommand.service.TransactionAttachment;
import org.mxhero.engine.plugin.storageapi.CloudStorage;
import org.mxhero.engine.plugin.storageapi.StorageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * The Class ConsumerQueue.
 */
public class ConsumerQueue implements Runnable, BeanFactoryAware {

	/** The bean. */
	private BeanFactory bean;
	
	/** The queue. */
	private final BlockingQueue<TransactionAttachment> queue;
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(ConsumerQueue.class);

	/**
	 * Instantiates a new consumer queue.
	 *
	 * @param queue the queue
	 */
	public ConsumerQueue(BlockingQueue<TransactionAttachment> queue) {
		this.queue = queue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		TransactionAttachment tx;
		CloudStorage storage = getCloudStorage();
		AttachmentService service = getService();
		try {
			logger.debug("Getting Tx from queue");
			tx = this.queue.poll(1, TimeUnit.SECONDS);
			if(tx != null){
				logger.debug("Uploading transaction {}", tx);
				StorageResult store = storage.store(tx.getEmail(), tx.getFilePath());
				if(store.isSuccess()){
					logger.debug("Tx uploaded success. Notify attachmentlinks");
					if(store.getFileStored()!=null){
						tx.setPublicUrl(store.getFileStored().getUrl());
					}
					service.sendMessage(tx, true);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the service.
	 *
	 * @return the service
	 */
	private AttachmentService getService(){
		return (AttachmentService) this.bean.getBean("attachmentLinkExternalService");
	}

	
	/**
	 * Gets the cloud storage.
	 *
	 * @return the cloud storage
	 */
	private CloudStorage getCloudStorage() {
		return this.bean.getBean(CloudStorage.class);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	@Override
	public void setBeanFactory(BeanFactory bean) throws BeansException {
		this.bean = bean;
	}



}

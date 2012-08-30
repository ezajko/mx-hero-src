package org.mxhero.engine.plugin.boxstorage.internal.sinchro;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.mxhero.engine.plugin.attachmentlink.alcommand.service.AttachmentService;
import org.mxhero.engine.plugin.attachmentlink.alcommand.service.TransactionAttachment;
import org.mxhero.engine.plugin.storageapi.CloudStorage;
import org.mxhero.engine.plugin.storageapi.StorageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ConsumerQueue.
 */
public class ConsumerQueue implements Runnable {

	/** The queue. */
	private BlockingQueue<TransactionAttachment> queue;
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(ConsumerQueue.class);

	/** The storage. */
	private CloudStorage storage;

	/** The service. */
	private AttachmentService service;
	
	ConsumerQueue(){}

	/**
	 * Creates the.
	 *
	 * @param queue the queue
	 * @param semaphore the semaphore
	 * @return the consumer queue
	 */
	public ConsumerQueue(BlockingQueue<TransactionAttachment> queue){
		this.setQueue(queue);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			while(true){
				logger.debug("Thread {} executing", Thread.currentThread().getName());
				logger.debug("Wait for Producer to put new transactions..");
				if(!this.getQueue().isEmpty()){
					logger.debug("Getting Tx from queue");
					TransactionAttachment tx = this.getQueue().poll(1, TimeUnit.SECONDS);
					if(tx != null){
						try {
							logger.debug("Uploading transaction {}", tx);
							StorageResult store = getStorage().store(tx.getEmail(), tx.getFilePath());
							if(store.isSuccess()){
								logger.debug("Tx uploaded success. Notify attachmentlinks");
								if(store.getFileStored()!=null){
									tx.setPublicUrl(store.getFileStored().getUrl());
								}
								getService().sendMessage(tx, true);
							}
						} catch (Exception e) {
							logger.error("Error message {}", e.getMessage());
							logger.error("Error class {}", e.getClass().getName());
						}
					}
				}else{
					Thread.sleep(5000);
				}
			}
		} catch (InterruptedException e) {
			logger.info("Shuting down consumer thread {}", Thread.currentThread().getName());
			logger.info("Thread {} was interrupted", Thread.currentThread().getName());
		}
	}

	/**
	 * Gets the storage.
	 *
	 * @return the storage
	 */
	public CloudStorage getStorage() {
		return storage;
	}

	/**
	 * Sets the storage.
	 *
	 * @param storage the new storage
	 */
	public void setStorage(CloudStorage storage) {
		this.storage = storage;
	}

	/**
	 * Gets the service.
	 *
	 * @return the service
	 */
	public AttachmentService getService() {
		return service;
	}

	/**
	 * Sets the service.
	 *
	 * @param service the new service
	 */
	public void setService(AttachmentService service) {
		this.service = service;
	}

	/**
	 * Gets the queue.
	 *
	 * @return the queue
	 */
	public BlockingQueue<TransactionAttachment> getQueue() {
		return queue;
	}

	/**
	 * Sets the queue.
	 *
	 * @param queue the new queue
	 */
	public void setQueue(BlockingQueue<TransactionAttachment> queue) {
		this.queue = queue;
	}

}

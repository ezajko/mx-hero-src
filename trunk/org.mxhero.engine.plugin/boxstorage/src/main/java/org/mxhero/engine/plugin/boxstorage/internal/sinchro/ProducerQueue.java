package org.mxhero.engine.plugin.boxstorage.internal.sinchro;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

import org.mxhero.engine.plugin.attachmentlink.alcommand.service.AttachmentService;
import org.mxhero.engine.plugin.attachmentlink.alcommand.service.TransactionAttachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ProducerQueue.
 */
public class ProducerQueue implements Runnable {

	/** The queue. */
	private BlockingQueue<TransactionAttachment> queue;
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(ConsumerQueue.class);


	/** The semaphore. */
	private Semaphore semaphore;
	
	/** The service. */
	private AttachmentService service;
	
	/** The transactions to retrieve. */
	private Integer transactionsToRetrieve;
	
	/**
	 * Instantiates a new producer queue.
	 */
	public ProducerQueue() {
	}
	/**
	 * Creates the.
	 *
	 * @param queue the queue
	 * @param semaphore the semaphore
	 * @return the producer queue
	 */
	public ProducerQueue(BlockingQueue<TransactionAttachment> queue, Semaphore semaphore){
		this.setQueue(queue);
		this.setSemaphore(semaphore);

	}


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			while(true){
				if(getQueue().isEmpty()){
					int availablePermits = getSemaphore().availablePermits();
					getSemaphore().acquire(availablePermits);
					logger.debug("Refilling queue");
					List<TransactionAttachment> transactions = getService().getTransactionToProcess(getTransactionsToRetrieve());
					this.getQueue().addAll(transactions);
					logger.debug("Notify consumer to start process transactions");
					getSemaphore().release(availablePermits);
				}else{
					Thread.sleep(5000);
				}
			}
		} catch (InterruptedException e) {
			logger.info("Shuting down producer thread {}", Thread.currentThread().getName());
			logger.error("Semphore in Producer was interrupted by another thread");
			logger.error("Error message {}", e.getMessage());
			logger.error("Error class {}", e.getClass().getName());
		}
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

	/**
	 * Gets the semaphore.
	 *
	 * @return the semaphore
	 */
	public Semaphore getSemaphore() {
		return semaphore;
	}

	/**
	 * Sets the semaphore.
	 *
	 * @param semaphore the new semaphore
	 */
	public void setSemaphore(Semaphore semaphore) {
		this.semaphore = semaphore;
	}
	
	/**
	 * Gets the transactions to retrieve.
	 *
	 * @return the transactions to retrieve
	 */
	public Integer getTransactionsToRetrieve() {
		return transactionsToRetrieve;
	}
	
	/**
	 * Sets the transactions to retrieve.
	 *
	 * @param transactionsToRetrieve the new transactions to retrieve
	 */
	public void setTransactionsToRetrieve(Integer transactionsToRetrieve) {
		this.transactionsToRetrieve = transactionsToRetrieve;
	}


}

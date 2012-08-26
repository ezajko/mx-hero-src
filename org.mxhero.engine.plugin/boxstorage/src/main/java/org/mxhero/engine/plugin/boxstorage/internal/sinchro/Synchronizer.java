package org.mxhero.engine.plugin.boxstorage.internal.sinchro;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.mxhero.engine.plugin.attachmentlink.alcommand.service.TransactionAttachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;


/**
 * The Class Synchronizer.
 */
public class Synchronizer implements BeanFactoryAware {
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(Synchronizer.class);
	
	/** The amount consumer threads. */
	private Integer amountConsumerThreads;

	/** The beans. */
	private BeanFactory beans;
	
	/**
	 * Synchronize.
	 */
	public void synchronize(){
		logger.debug("Start new synchronization running....");
		BlockingQueue<TransactionAttachment> queue = new LinkedBlockingDeque<TransactionAttachment>(100);
		ExecutorService producerThreads = Executors.newSingleThreadExecutor();
		producerThreads.execute(getProducer(queue));
		producerThreads.shutdown();
		try {
			producerThreads.awaitTermination(3, TimeUnit.MINUTES);
			logger.debug("Producer finishing filling the queue");
			ExecutorService consumerThreads = Executors.newFixedThreadPool(getAmountConsumerThreads());
			consumerThreads.execute(getConsumer(queue));
			consumerThreads.shutdown();
			consumerThreads.awaitTermination(30, TimeUnit.MINUTES);
			logger.debug("Synchronization finishing successfully");
		} catch (InterruptedException e) {
			logger.error("Error in synchronization with box");
			logger.error("Error message {}", e.getMessage());
			logger.error("Error class {}", e.getClass().getSimpleName());
		}
	}

	/**
	 * Gets the consumer.
	 *
	 * @param queue the queue
	 * @return the consumer
	 */
	private Runnable getConsumer(BlockingQueue<TransactionAttachment> queue) {
		return (Runnable) this.beans.getBean("consumer", queue);
	}

	/**
	 * Gets the producer.
	 *
	 * @param queue the queue
	 * @return the producer
	 */
	private Runnable getProducer(BlockingQueue<TransactionAttachment> queue) {
		return (Runnable) this.beans.getBean("producer", queue);
	}

	/**
	 * Gets the amount consumer threads.
	 *
	 * @return the amount consumer threads
	 */
	public Integer getAmountConsumerThreads() {
		return amountConsumerThreads;
	}

	/**
	 * Sets the amount consumer threads.
	 *
	 * @param amountConsumerThreads the new amount consumer threads
	 */
	public void setAmountConsumerThreads(Integer amountConsumerThreads) {
		this.amountConsumerThreads = amountConsumerThreads;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	@Override
	public void setBeanFactory(BeanFactory beans) throws BeansException {
		this.beans = beans;
	}
}

package org.mxhero.engine.plugin.boxstorage.internal.sinchro;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

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

	/** The queue capacity. */
	private Integer queueCapacity;
	
	/** The beans. */
	private BeanFactory beans;

	/** The consumer threads. */
	private ExecutorService consumerThreads;

	/** The producer threads. */
	private ExecutorService producerThreads;

	/**
	 * Synchronize.
	 */
	public void start(){
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				logger.debug("Start new synchronization running....");
				BlockingQueue<TransactionAttachment> queue = new LinkedBlockingDeque<TransactionAttachment>(getQueueCapacity());
				producerThreads = Executors.newSingleThreadExecutor();
				producerThreads.execute(getProducer(queue));
				logger.debug("Producer finishing filling the queue");
				consumerThreads = Executors.newFixedThreadPool(getAmountConsumerThreads());
				for (int i = 0; i < getAmountConsumerThreads(); i++) {
					Runnable consumer = getConsumer(queue);
					consumerThreads.execute(consumer);
				}
			}
		});
		thread.start();
	}
	
	/**
	 * Stop.
	 */
	public void stop(){
		producerThreads.shutdownNow();
		consumerThreads.shutdownNow();
		logger.warn("SHUTING DOWN SYNCHRONIZER. YOU MUST RESTART THE APPLICATION IN ORDER TO RE RUN SYNCHRONIZATION");
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

	public Integer getQueueCapacity() {
		return queueCapacity;
	}

	public void setQueueCapacity(Integer queueCapacity) {
		this.queueCapacity = queueCapacity;
	}
}

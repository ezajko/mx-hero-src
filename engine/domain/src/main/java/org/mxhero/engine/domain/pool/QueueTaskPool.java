package org.mxhero.engine.domain.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 
 * This class is used to implements a ThreadPool based on tasks that
 * are created from object polled from a queue.
 * @author mmarmol
 * @param <T> Type of the object that is pooled of the queue.
 */
public abstract class QueueTaskPool<T> extends TaskPool{

	private BlockingQueue<T> queue;
	
	/**
	 * Constructor that receives the queue where object to be processed are.
	 * @param queue
	 */
	public QueueTaskPool(BlockingQueue<T> queue){
		this.queue = queue;
	}

	/**
	 * Will try to get and object of type <T> from the queue, if this 
	 * object is not null it will call createTask(object) to get the task 
	 * to be added to the pool if that task is not null. If task is null 
	 * the object will be added again into the queue.
	 */
	@Override
	protected void work(){
		T object = null;
		Runnable task = null;
		try{
			object = queue.poll(getWaitTime(), TimeUnit.MILLISECONDS);
			if (object!=null){
				task = createTask(object);
				if (task != null){
					getExecutor().execute(task);
				} else {
					queue.offer(object);
					Thread.sleep(getWaitTime());
				}
			}
		} catch (InterruptedException e){
			if (object!=null){
				if(task==null){
					queue.offer(object);
				} else {
					if(!getExecutor().getQueue().contains(task)){
						queue.offer(object);
					}
				}
			}
		}
	}
	
	/**
	 * This is the method factory for task based on the object <T>.
	 * @param object of type <T>
	 * @return the task to be added to the ThreadPool or null if the task should not be created.
	 */
	protected abstract Runnable createTask(T object);
	
	
}

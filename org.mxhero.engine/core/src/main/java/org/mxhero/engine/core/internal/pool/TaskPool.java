package org.mxhero.engine.core.internal.pool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author mmarmol
 */
public abstract class TaskPool implements Runnable {

	private static final long DEFAULT_WAITTIME = 1000;
	private static final int DEFAULT_COREPOOLSIZE = 3;
	private static final int DEFAULT_MAXIMUMPOOLSIZE = 12;
	private static final long DEFAULT_KEEPALIVETIME = 2000;
	
	private ThreadPoolExecutor executor;

	private Thread thread;
	
	private boolean running = false;
	
	private long waitTime = DEFAULT_WAITTIME;
	
	public TaskPool() {
		executor=new ThreadPoolExecutor(DEFAULT_COREPOOLSIZE, DEFAULT_MAXIMUMPOOLSIZE, DEFAULT_KEEPALIVETIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}

	/** 
	 * Starts the ThreadPool calls init method and keep calling work method while
	 * running boolean is true, after that shutdowns the ThreadPool and call clean method.
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public final void run() {
		if(executor==null){
			executor=new ThreadPoolExecutor(DEFAULT_COREPOOLSIZE, DEFAULT_MAXIMUMPOOLSIZE, DEFAULT_KEEPALIVETIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		}
		init();
		running=true;
		while(isRunning()){
			work();
		}
		executor.shutdown();
		clean();
	}
	
	/**
	 * Waits the waitTime.
	 */
	protected void work(){
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {}
	}
	
	/**
	 * Creates a thread, pass this instance and start to work.
	 */
	public void start(){
		thread = new Thread(this);
		thread.start();
	}
	
	/**
	 * Implement here logic before the start working.
	 */
	protected abstract void init();
	
	/**
	 * Implement here logic after stops working.
	 */
	protected abstract void clean();

	/**
	 * Sets the running boolean to false, after that work method will stop.
	 */
	public final void stop(){
		running = false;
		if(thread!=null){
			try {
				thread.join();
			} catch (InterruptedException e) {
			}
			thread=null;
		}
		this.executor=null;
	}
	
	/**
	 * Returns if this thread is looping the work method.
	 * @return 
	 */
	public final boolean isRunning(){
		return running;
	}
	
	/**
	 * @param corePoolsize
	 */
	public final void setCorePoolsize(Integer corePoolsize) {
		try{
			if(corePoolsize!=null && this.executor!=null){
				this.executor.setCorePoolSize(corePoolsize);
			}
		}
		catch (IllegalArgumentException e){}
	}

	public final Integer getCorePoolsize(){
		return this.executor.getCorePoolSize();
	}
	
	/**
	 * @param maximumPoolSize
	 */
	public final void setMaximumPoolSize(Integer maximumPoolSize) {
		try{
			if(maximumPoolSize!=null && this.executor!=null){
				this.executor.setMaximumPoolSize(maximumPoolSize);
			}
		} 
		catch (IllegalArgumentException e){}
	}

	public final Integer getMaximumPoolSize(){
		return this.executor.getMaximumPoolSize();
	}
	
	/**
	 * @param keepAliveTime
	 */
	public final void setKeepAliveTime(Long keepAliveTime) {
		try{
			if(keepAliveTime!=null && this.executor!=null){
				this.executor.setKeepAliveTime(keepAliveTime,TimeUnit.MILLISECONDS);
			}
		}		
		catch (IllegalArgumentException e){}
	}

	public final Long getKeepAliveTime(){
		return this.executor.getKeepAliveTime(TimeUnit.MILLISECONDS);
	}
	
	/**
	 * @param waitTime
	 */
	public final void setWaitTime(Long waitTime){
		try{
			if(waitTime!=null){
				long value = waitTime;
				if(value>0){
					this.waitTime= value;
				}
			}
		}		
		catch (NumberFormatException e){}	
	}

	/**
	 * @return
	 */
	public final long getWaitTime() {
		return waitTime;
	}

	/**
	 * @return
	 */
	public final ThreadPoolExecutor getExecutor() {
		return executor;
	}

	
}

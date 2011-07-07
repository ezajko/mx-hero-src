package org.mxhero.engine.mqueues.internal.queue.blocking;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

import org.mxhero.engine.mqueues.internal.queue.QueueId;


public abstract class AbstractMimeMailBlockingQueue<E>{

	private final QueueId id;
	
	/** The capacity bound, or Integer.MAX_VALUE if none */
    private final int capacity;

    /** Current number of elements */
    private final AtomicInteger count = new AtomicInteger(0);
    
    /** Current number of elements */
    private final AtomicInteger enqueued = new AtomicInteger(0);

    /** Lock held by take, poll, etc */
    private final ReentrantLock dequeueLock = new ReentrantLock();
    
    /** Wait queue for waiting takes, take, poll, etc */
    private final Condition notEmpty = dequeueLock.newCondition();
    
    /** Lock held by take, poll, etc */
    private final ReentrantLock takeLock = new ReentrantLock();

    /** Lock held by put, offer, etc */
    private final ReentrantLock putLock = new ReentrantLock();

    /** Wait queue for waiting puts */
    private final Condition notFull = putLock.newCondition();

    /**
     * Signals a waiting take. Called only from put/offer (which do not
     * otherwise ordinarily lock takeLock.)
     */
    private void signalNotEmpty() {
        final ReentrantLock dequeueLock = this.dequeueLock;
        dequeueLock.lock();
        try {
            notEmpty.signal();
        } finally {
        	dequeueLock.unlock();
        }
    }

    /**
     * Signals a waiting put. Called only from take/poll.
     */
    private void signalNotFull() {
        final ReentrantLock putLock = this.putLock;
        putLock.lock();
        try {
            notFull.signal();
        } finally {
            putLock.unlock();
        }
    }

    /**
     * Creates a node and add it to the end of queue.
     * @param x the item
     */
    abstract protected void enqueue(E x);

    /**
     * Removes a node from head of queue.
     * @return the node
     */
    abstract protected E dequeue(); 

    /**
     * Lock to prevent both puts and takes.
     */
    void fullyLock() {
        putLock.lock();
        takeLock.lock();
    }


    /**
     * Creates a queue with the given (fixed) capacity.
     *
     * @param capacity the capacity of this queue
     * @throws IllegalArgumentException if <tt>capacity</tt> is not greater
     *         than zero
     */
    protected AbstractMimeMailBlockingQueue(QueueId id, int capacity, int count) {
        if (capacity <= 0) throw new IllegalArgumentException();
        if (count < 0 || count > capacity) throw new IllegalArgumentException();
        this.capacity = capacity;
        this.count.set(count);
        this.enqueued.set(count);
		if(id == null 
				|| id.getModule()==null 
				|| id.getPhase()==null 
				|| id.getModule().trim().length()<0
				|| id.getPhase().trim().length()<0) 
				throw new IllegalArgumentException();
		this.id=id;
    }


    /**
     * Returns the number of elements in this queue.
     *
     * @return the number of elements in this queue
     */
    public int size() {
        return count.get();
    }

    
    /**
     * Returns the number of elements enqueued in this queue.
     *
     * @return the number of elements in this queue
     */
    public int enqueued() {
        return enqueued.get();
    }
    
    /**
     * Returns the number of additional elements that this queue can ideally
     * (in the absence of memory or resource constraints) accept without
     * blocking. This is always equal to the initial capacity of this queue
     * less the current <tt>size</tt> of this queue.
     */
    public int remainingCapacity() {
        return capacity - count.get();
    }

    /**
     * Inserts the specified element at the tail of this queue, waiting if
     * necessary for space to become available.
     *
     * @throws InterruptedException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    public void put(E e) throws InterruptedException {
        if (e == null) throw new NullPointerException();
        int c = -1;
        int ec = -1;
        final ReentrantLock putLock = this.putLock;
        final AtomicInteger count = this.count;
        final AtomicInteger enqueued = this.enqueued;
        putLock.lockInterruptibly();
        try {
            while (count.get() == capacity) { 
                    notFull.await();
            }
            enqueue(e);
            c = count.getAndIncrement();
            ec = enqueued.getAndIncrement();
            if (c + 1 < capacity)
                notFull.signal();
        } finally {
            putLock.unlock();
        }
        if (ec == 0)
            signalNotEmpty();
    }

    public void reEnqueue(E e, long time) throws InterruptedException {
        if (e == null) throw new NullPointerException();
        int ec = -1;
        final AtomicInteger enqueued = this.enqueued;

        while (count.get() == capacity) { 
                notFull.await();
        }
        if(realReEnqueue(e, time)){
            ec = enqueued.getAndIncrement();
        }else{
        	throw new IllegalArgumentException();
        }
        if (ec == 0)
            signalNotEmpty();
    }

    protected abstract boolean realReEnqueue(E o, long time);
    
    /**
     * Inserts the specified element at the tail of this queue, waiting if
     * necessary up to the specified wait time for space to become available.
     *
     * @return <tt>true</tt> if successful, or <tt>false</tt> if
     *         the specified waiting time elapses before space is available.
     * @throws InterruptedException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    public boolean offer(E e, long timeout, TimeUnit unit)
        throws InterruptedException {

        if (e == null) throw new NullPointerException();
        long nanos = unit.toNanos(timeout);
        int c = -1;
        int ec = -1;
        final ReentrantLock putLock = this.putLock;
        final AtomicInteger count = this.count;
        final AtomicInteger enqueued = this.enqueued;
        putLock.lockInterruptibly();
        try {
            while (count.get() == capacity) {

                if (nanos <= 0)
                    return false;
                nanos = notFull.awaitNanos(nanos);
            }
            enqueue(e);
            c = count.getAndIncrement();
            ec = enqueued.getAndIncrement();
           if (c + 1 < capacity)
               notFull.signal();
        } finally {
            putLock.unlock();
        }
        if (ec == 0)
            signalNotEmpty();
        return true;
    }

    /**
     * Inserts the specified element at the tail of this queue if it is
     * possible to do so immediately without exceeding the queue's capacity,
     * returning <tt>true</tt> upon success and <tt>false</tt> if this queue
     * is full.
     * @throws NullPointerException if the specified element is null
     */
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        final AtomicInteger count = this.count;
        final AtomicInteger enqueued = this.enqueued;
        if (count.get() == capacity)
            return false;
        int c = -1;
        int ec = -1;
        final ReentrantLock putLock = this.putLock;
        putLock.lock();
        try {
            if (count.get() < capacity) {
                enqueue(e);
                c = count.getAndIncrement();
                ec = enqueued.getAndIncrement();
                if (c + 1 < capacity)
                    notFull.signal();
            }
        } finally {
            putLock.unlock();
        }
        if (ec == 0)
            signalNotEmpty();
        return c >= 0;
    }


    public E take() throws InterruptedException {
        E x;
        int ec = -1;
        final AtomicInteger enqueued = this.enqueued;
        final ReentrantLock dequeueLock = this.dequeueLock;
        dequeueLock.lockInterruptibly();
        try {
                while (enqueued.get() == 0) {
                    notEmpty.await();
                }
            x = dequeue();
            if(x!=null){
	            ec = enqueued.getAndDecrement();
	            if (ec > 1)
	                notEmpty.signal();
            }
        } finally {
        	dequeueLock.unlock();
        }

        return x;
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        E x = null;
        int ec = -1;
        final AtomicInteger enqueued = this.enqueued;
        long nanos = unit.toNanos(timeout);
        final ReentrantLock dequeueLock = this.dequeueLock;
        dequeueLock.lockInterruptibly();
        try {
                while (enqueued.get() == 0) { 
                  if (nanos <= 0)
                    return null;
                  nanos = notEmpty.awaitNanos(nanos);
                }
                x = dequeue();
                if(x!=null){
	                ec = enqueued.getAndDecrement();
	                if (ec > 1)
	                    notEmpty.signal();
                }
        } finally {
        	dequeueLock.unlock();
        }
        return x;
    }

    public E poll() {
        int ec = -1;
        final AtomicInteger enqueued = this.enqueued;
        if (enqueued.get() == 0)
            return null;
        E x = null;
        final ReentrantLock dequeueLock = this.dequeueLock;
        dequeueLock.lock();
        try {
            if (enqueued.get() > 0) {
                x = dequeue();
                if(x!=null){
	                ec = enqueued.getAndDecrement();
	                if (ec > 1)
	                    notEmpty.signal();
                }
            }
        } finally {
        	dequeueLock.unlock();
        }
        return x;
    }

    /**
     * Removes a single instance of the specified element from this queue,
     * if it is present. Element must be dequeued
     * @param o element to be removed from this queue, if present and is dequeued
     * @return <tt>true</tt> if this queue changed as a result of the call
     */
     public boolean remove(E o) throws InterruptedException{
    	 if (o == null) throw new NullPointerException();
         int c = -1;
         boolean returnValue = false;
         final AtomicInteger count = this.count;
    	 final ReentrantLock takeLock = this.takeLock;
         takeLock.lock();
    	 try{
    		 if(realRemove(o)){
	    		 c = count.getAndDecrement();
	    	     returnValue = true;
	    	 }
         } finally {
             takeLock.unlock();
         }
	     if (c == capacity){
	    	 signalNotFull();
	     }
    	 return returnValue;
     }
     
	abstract protected boolean realRemove(E o);


    /**
     * Remove a MimeMail from this queue physical, but only if email is not enqueued anymore
     * @param o MimeMail to remove, it must not be enqueued
     * @param z MimeMail to add to the other queue
     * @param queueTo queue where MimeMail is going to be add
     * @return
     * @throws InterruptedException 
     */
    public void removeAddTo(E o, E z, AbstractMimeMailBlockingQueue<E> queueTo) throws InterruptedException{
        if (o == null) throw new NullPointerException();;

        final AtomicInteger count = this.count;
        final AtomicInteger queueToCount = queueTo.getCount();
        final AtomicInteger queueToEnqueuedCount = queueTo.getEnqueued();
        int c = -1;
        int ec = -1;
        int ic = -1;

        final ReentrantLock queueToPutLock = queueTo.getPutLock();
        final ReentrantLock takeLock = this.takeLock;
        final Condition queueToNotFull = queueTo.getNotFull();
        queueToPutLock.lock();
        try {
			while (queueToCount.get() == queueTo.getCapacity()) { 
				queueToNotFull.await();
			}		
			takeLock.lock();
			try{
				if(realRemoveAddTo(o,z,queueTo)){
					c = queueToCount.getAndIncrement();
					ec = queueToEnqueuedCount.getAndIncrement();
					ic = count.getAndDecrement();
					if (c + 1 < queueTo.getCapacity()){
						queueTo.getNotFull().signal();
					}
				}
			}finally{
				takeLock.unlock();
			}
        } finally {
        	queueToPutLock.unlock();
        }
        if (ec == 0)
        	queueTo.signalNotEmpty();
	     if (ic == capacity){
	    	 signalNotFull();
	     }
    }
    
    abstract protected boolean realRemoveAddTo(E o, E z, AbstractMimeMailBlockingQueue<E> queueTo);

	public QueueId getId() {
		return id;
	}

	 AtomicInteger getCount() {
		return count;
	}

	 AtomicInteger getEnqueued() {
		return enqueued;
	}

	 Condition getNotFull() {
		return notFull;
	}

	 ReentrantLock getPutLock() {
		return putLock;
	}

	public int getCapacity() {
		return capacity;
	}
	
}
package org.mxhero.engine.plugin.boxserver.caching;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;

// TODO: Auto-generated Javadoc
/**
 * The Class TicketCaching.
 */
public class TicketCaching {

	/** The cache. */
	private ConcurrentMap<Object, Object> cache;
	
	/**
	 * Instantiates a new ticket caching.
	 */
	public TicketCaching(){
		cache = CacheBuilder.newBuilder().maximumSize(1000).concurrencyLevel(4).expireAfterAccess(10, TimeUnit.MINUTES).expireAfterWrite(10, TimeUnit.MINUTES).build().asMap();
	}

	
	/**
	 * Gets the url.
	 *
	 * @param ticket the ticket
	 * @return the url
	 */
	public Application getApp(String ticket){
		return (Application) cache.get(ticket);
	}
	
	/**
	 * Sets the url.
	 *
	 * @param ticket the ticket
	 * @param url the url
	 */
	public void setUrl(String ticket, String url, String appKey){
		cache.put(ticket, new Application(url, appKey));
	}
	
}

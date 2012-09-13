/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.feature.replytimeout.provider.internal.noreplycheck;

import org.mxhero.engine.plugin.threadlight.pagination.common.PageResult;
import org.mxhero.engine.plugin.threadlight.service.ThreadRowService;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRowFollower;
import org.mxhero.feature.replytimeout.provider.internal.Provider;
import org.mxhero.feature.replytimeout.provider.internal.config.ReplyTimeoutConfig;
import org.mxhero.feature.replytimeout.provider.internal.noreplycheck.message.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoreplyCheckRunner implements Runnable{

	private static Logger log = LoggerFactory.getLogger(NoreplyCheckRunner.class);
	
	private static final long CHECK_TIME = 1000;
	private Thread thread;
	private boolean keepWorking = false;
	private ReplyTimeoutConfig config;
	private ThreadRowService service;
	private MessageSender sender;
	
	public void start(){
		thread=new Thread(this);
		keepWorking=true;
		thread.start();
	}

	public void stop(){
		keepWorking=false;	
		if(thread!=null){
			try {
				thread.join();
			} catch (InterruptedException e) {}
		}
	}

	@Override
	public void run() {
		long lastCheck = System.currentTimeMillis();
		while(keepWorking){
			try {
				Thread.sleep(CHECK_TIME);
			} catch (InterruptedException e) {}
			if(lastCheck+(config.getCheckTimeInMinutes()*60*1000)-System.currentTimeMillis()<0){
				work();
				lastCheck=System.currentTimeMillis();
			}
		}
	}
	
	public void work(){
		PageResult<ThreadRow> threads = null;
		int pageNumber = 1;
		ThreadRow filterRow = new ThreadRow();
		try{
			do{
				threads = service.findByParameters(filterRow, Provider.FOLLOWER_ID, pageNumber, 50);
				++pageNumber;
				log.debug("cheking" +threads.getTotalRecordsNumber()+" threads.");
				if(threads!=null){
					for(ThreadRow row : threads.getPageData()){
						try{
							if(row.getFollowers()!=null && row.getFollowers().size()>0){
								ThreadRowFollower follower = row.getFollowers().iterator().next();
								Long timeout = Long.parseLong(follower.getFolowerParameters().split(";")[0]);
								if(timeout<System.currentTimeMillis() && sender.createAndSend(row)){
									log.debug("sent message from thread "+row);
									service.unfollow(row.getPk(), Provider.FOLLOWER_ID);
								}							
							}
						}catch(Exception e){
							log.warn("error while checking thread "+row);
						}
					}
				}
			}while(threads!=null && threads.getPageAmount()>threads.getPageNumber());
		}catch(Exception e){
			log.warn("error while checking for noreply messages",e);
		}
	}

	public ThreadRowService getService() {
		return service;
	}

	public void setService(ThreadRowService service) {
		this.service = service;
	}

	public MessageSender getSender() {
		return sender;
	}

	public void setSender(MessageSender sender) {
		this.sender = sender;
	}

	public ReplyTimeoutConfig getConfig() {
		return config;
	}

	public void setConfig(ReplyTimeoutConfig config) {
		this.config = config;
	}

}

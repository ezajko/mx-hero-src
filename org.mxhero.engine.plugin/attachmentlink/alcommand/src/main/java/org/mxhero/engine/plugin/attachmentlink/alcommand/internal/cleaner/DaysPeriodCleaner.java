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

package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.cleaner;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class DaysPeriodCleaner {

	private static Logger log = Logger.getLogger(DaysPeriodCleaner.class);
	
	private Integer daysPeriod = 30;

	private Integer checkTimeInHours = 12;
	
	private Integer chechForStop = 1000;
	
	private Thread thread;
	
	private long lastWorkTime = 0;
	
	private Boolean isRunning=false;

	private NamedParameterJdbcTemplate template;
	
	private String checkString = " SELECT m.`message_id` " +
			" FROM attachments.`message` m INNER JOIN attachments.`message_attach` ma " +
			" ON ma.`message_id` = m.`message_id` " +
			" GROUP BY m.`message_id` " +
			" HAVING MAX(ma.`creation_date`) < ? ";
	
	@Autowired
	public DaysPeriodCleaner(DataSource ds){
		this.template = new NamedParameterJdbcTemplate(ds);
	}
	
	public void init(){
		if(!isRunning){
			thread = new Thread(new Worker());
			thread.start();
			isRunning=true;
		}
	}
	
	public void stop(){
		isRunning = false;
		if(thread!=null){
			try {
				thread.join();
			} catch (InterruptedException e) {
			}
			thread=null;
		}
	}
	
	public void work(){
		Calendar since = Calendar.getInstance();
		since.add(Calendar.DAY_OF_MONTH, -daysPeriod);
		log.debug("cheking messages for date :"+since.getTime().toString());
		List<Long> oldMessages = template.getJdbcOperations().queryForList(checkString,Long.class,since.getTime());
		if(oldMessages!=null){
			log.debug("found X messages:"+oldMessages.size());
			for(Long messageId : oldMessages){
				List<Long> messageAttachs = template.getJdbcOperations().queryForList("SELECT `message_attach_id` FROM attachments.`message_attach` WHERE `message_id` = ?",Long.class, messageId);
				if(messageAttachs!=null){
					log.debug("found message attachs:"+messageAttachs.size());
					for(Long messageAttahcId : messageAttachs){
						int deletedHistory = template.getJdbcOperations().update("DELETE FROM attachments.`history_access_attach` WHERE `message_attach_id` = ?", messageAttahcId);
						log.debug("deleted histories:"+deletedHistory);
						int deletedMessageAttach = template.getJdbcOperations().update("DELETE FROM attachments.`message_attach` WHERE `message_attach_id` = ?", messageAttahcId);
						log.debug("deleted histories:"+deletedMessageAttach);
					}
				}
				template.getJdbcOperations().update("DELETE FROM attachments.`message` WHERE `message_id` = ?", messageId);
				log.debug("deleted message:"+messageId);
			}		
		}
		String findAttachs =" SELECT a.`attach_id`, a.`path` " +
				" FROM attachments.`attach` a " +
				" WHERE NOT EXISTS (SELECT 1 " +
									" FROM attachments.`message_attach` ma" +
									" WHERE ma.`attach_id` = a.`attach_id`)";
		List<Map<String, Object>> unlinkedAttachs = template.getJdbcOperations().queryForList(findAttachs);
		if(unlinkedAttachs!=null){
			log.debug("found X unlinked attachs:"+unlinkedAttachs.size());
			for(Map<String, Object> attach : unlinkedAttachs){
				int deletedAttach = template.getJdbcOperations().update("DELETE FROM attachments.`attach` WHERE `attach_id` = ?", attach.get("attach_id"));
				log.debug("attach record deleted:"+deletedAttach);
				try{
					File attachFile = new File(attach.get("path").toString());
					if(attachFile.exists()){
						attachFile.delete();
						log.debug("file deted:"+attach.get("path").toString());
					}else{
						log.warn("file no exists:"+attach.get("path").toString());
					}
				}catch(Exception e){
					log.error("error deleting attach file",e);
				}
			}
		}	
	}
	
	public Integer getDaysPeriod() {
		return daysPeriod;
	}

	public void setDaysPeriod(Integer daysPeriod) {
		this.daysPeriod = daysPeriod;
	}

	public Integer getCheckTimeInHours() {
		return checkTimeInHours;
	}

	public void setCheckTimeInHours(Integer checkTimeInHours) {
		this.checkTimeInHours = checkTimeInHours;
	}
	
	private class Worker implements Runnable{

		@Override
		public void run() {
			while(isRunning){
				if(System.currentTimeMillis()-lastWorkTime>checkTimeInHours*60*60*1000){
					work();
					lastWorkTime=System.currentTimeMillis();
				}
				try {
					Thread.sleep(chechForStop);
				} catch (InterruptedException e){}
			}
		}

	}

	public Boolean isRunning() {
		return isRunning;
	}
	
}

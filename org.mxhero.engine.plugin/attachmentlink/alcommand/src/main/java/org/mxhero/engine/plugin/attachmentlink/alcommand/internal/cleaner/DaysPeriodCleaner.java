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
	
	private String checkString = " SELECT `message`.`message_id` " +
			" FROM `message`INNER JOIN `message_attach` " +
			" ON `message_attach`.`message_id` = `message`.`message_id` " +
			" GROUP BY `message`.`message_id` " +
			" HAVING MAX(`message_attach`.`creation_date`) < ? ";
	
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
				List<Long> messageAttachs = template.getJdbcOperations().queryForList("SELECT `message_attach_id` FROM `message_attach` WHERE `message_id` = ?",Long.class, messageId);
				if(messageAttachs!=null){
					log.debug("found message attachs:"+messageAttachs.size());
					for(Long messageAttahcId : messageAttachs){
						int deletedHistory = template.getJdbcOperations().update("DELETE FROM `history_access_attach` WHERE `message_attach_id` = ?", messageAttahcId);
						log.debug("deleted histories:"+deletedHistory);
						int deletedMessageAttach = template.getJdbcOperations().update("DELETE FROM `message_attach` WHERE `message_attach_id` = ?", messageAttahcId);
						log.debug("deleted histories:"+deletedMessageAttach);
					}
				}
				template.getJdbcOperations().update("DELETE FROM `message` WHERE `message_id` = ?", messageId);
				log.debug("deleted message:"+messageId);
			}		
		}
		String findAttachs =" SELECT `attach`.`attach_id`, `attach`.`path` " +
				" FROM `attach` " +
				" WHERE NOT EXISTS (SELECT 1 " +
									" FROM `message_attach` " +
									" WHERE `message_attach`.`attach_id` = `attach`.`attach_id`)";
		List<Map<String, Object>> unlinkedAttachs = template.getJdbcOperations().queryForList(findAttachs);
		if(unlinkedAttachs!=null){
			log.debug("found X unlinked attachs:"+unlinkedAttachs.size());
			for(Map<String, Object> attach : unlinkedAttachs){
				int deletedAttach = template.getJdbcOperations().update("DELETE FROM `attach` WHERE `attach_id` = ?", attach.get("attach_id"));
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

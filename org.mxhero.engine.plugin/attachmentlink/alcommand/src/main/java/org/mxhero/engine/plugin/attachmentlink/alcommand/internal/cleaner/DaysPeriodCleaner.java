package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.cleaner;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.ibm.icu.util.Calendar;

public class DaysPeriodCleaner {

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
		List<Long> oldMessages = template.getJdbcOperations().queryForList(checkString,Long.class,since.getTime());
		if(oldMessages!=null){
			for(Long messageId : oldMessages){
				List<Long> messageAttachs = template.getJdbcOperations().queryForList("SELECT `message_attach_id` FROM `message_attach` WHERE `message_id` = ?",Long.class, messageId);
				if(messageAttachs!=null){
					for(Long messageAttahcId : messageAttachs){
						template.getJdbcOperations().update("DELETE FROM `history_access_attach` WHERE `message_attach_id` = ?", messageAttahcId);
						template.getJdbcOperations().update("DELETE FROM `message_attach` WHERE `message_attach_id` = ?", messageAttahcId);
					}
				}
				template.getJdbcOperations().update("DELETE FROM `message` WHERE `message_id` = ?", messageId);
			}		
		}
		String findAttachs =" SELECT `attach`.`attach_id`, `attach`.`path` " +
				" FROM `attach` " +
				" WHERE NOT EXISTS (SELECT 1 " +
									" FROM `message_attach` " +
									" WHERE `message_attach`.`attach_id` = `attach`.`attach_id`)";
		List<Map<String, Object>> unlinkedAttachs = template.getJdbcOperations().queryForList(findAttachs);
		if(unlinkedAttachs!=null){
			for(Map<String, Object> attach : unlinkedAttachs){
				template.getJdbcOperations().update("DELETE FROM `attach` WHERE `attach_id` = ?", attach.get("attach_id"));
				File attachFile = new File(attach.get("path").toString());
				if(attachFile.exists()){
					attachFile.delete();
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

package org.mxhero.engine.plugin.statistics.internal.service;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.statistic.LogStat;
import org.mxhero.engine.plugin.statistics.internal.dao.RecordDao;
import org.mxhero.engine.plugin.statistics.internal.dao.StatDao;
import org.mxhero.engine.plugin.statistics.internal.entity.Record;
import org.mxhero.engine.plugin.statistics.internal.entity.RecordPk;
import org.mxhero.engine.plugin.statistics.internal.entity.Stat;
import org.mxhero.engine.plugin.statistics.internal.entity.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the LogStat interface using JPA.
 * @author mmarmol
 */
@Transactional
public class LogStatService implements LogStat{

	private static Logger log = LoggerFactory.getLogger(LogStatService.class);
	
	@Autowired
	private StatDao dao;
	
	@Autowired
	private RecordDao recordDao;
	
	/**
	 * @see org.mxhero.engine.domain.statistic.LogStat#log(org.mxhero.engine.domain.mail.MimeMail, java.lang.String, java.lang.String)
	 */
	@Override
	public void log(MimeMail mail, String key, String value) {
		if (mail==null || key==null){
			log.warn("Null values, can't be saved");
			return;
		}
		
		RecordPk recordPk = new RecordPk();
		recordPk.setSequence(mail.getSequence());
		recordPk.setInsertDate(mail.getTime());
		Stat stat = Utils.createStat(mail, key, value);
		
		try{
			Record record = getRecordDao().readByPrimaryKey(recordPk);
			stat.setRecord(record);
			getDao().save(stat);
		} catch(Exception e) {
			log.warn("Error while persisting "+stat,e);
		}
		
	}

	/**
	 * @return the dao
	 */
	public StatDao getDao() {
		return dao;
	}

	/**
	 * @param dao the dao to set
	 */
	public void setDao(StatDao dao) {
		this.dao = dao;
	}

	/**
	 * @return the recordDao
	 */
	public RecordDao getRecordDao() {
		return recordDao;
	}

	/**
	 * @param recordDao the recordDao to set
	 */
	public void setRecordDao(RecordDao recordDao) {
		this.recordDao = recordDao;
	}

}

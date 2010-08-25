package org.mxhero.engine.plugin.statistics.internal.service;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.statistic.LogRecord;
import org.mxhero.engine.plugin.statistics.internal.dao.RecordDao;
import org.mxhero.engine.plugin.statistics.internal.entity.Record;
import org.mxhero.engine.plugin.statistics.internal.entity.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the LogRecord interface using JPA.
 * @author mmarmol
 */
@Transactional
public class LogRecordService implements LogRecord{

	private static Logger log = LoggerFactory.getLogger(LogRecordService.class);
	
	@Autowired
	private RecordDao dao;
	
	/**
	 * @see org.mxhero.engine.domain.statistic.LogRecord#log(org.mxhero.engine.domain.mail.MimeMail)
	 */
	public void log(MimeMail mail){
		if (mail==null){
			log.warn("Mail is null, can't be saved");
			return;
		}
		Record record = Utils.createRecord(mail);
		try{
			getDao().save(record);
		} catch(Exception e) {
			log.warn("Error while persisting "+record,e);
		}
	}

	/**
	 * @return the dao
	 */
	public RecordDao getDao() {
		return dao;
	}

	/**
	 * @param dao the dao to set
	 */
	public void setDao(RecordDao dao) {
		this.dao = dao;
	}

}

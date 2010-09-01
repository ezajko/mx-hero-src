package org.mxhero.engine.plugin.statistics.internal.command;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.command.Result;
import org.mxhero.engine.plugin.statistics.command.LogStat;
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
 * 
 * @author mmarmol
 */
@Transactional
public class JpaLogStat implements LogStat{

	private static Logger log = LoggerFactory.getLogger(JpaLogStat.class);
	
	@Autowired(required=true)
	private StatDao dao;
	
	@Autowired(required=true)
	private RecordDao recordDao;
	
	/**
	 * @see org.mxhero.engine.domain.mail.command.Command#exec(org.mxhero.engine.domain.mail.MimeMail, java.lang.String[])
	 */
	@Override
	public Result exec(MimeMail mail, String... args) {
		Result result = new Result();
		result.setResult(false);
		
		if (args==null || args.length!=2 || args[0]==null || args[0].isEmpty() || args[1]==null || args[1].isEmpty()){
			log.warn("wrong params");
			return result;
		}
		RecordPk recordPk = new RecordPk();
		recordPk.setSequence(mail.getSequence());
		recordPk.setInsertDate(mail.getTime());
		Stat stat = Utils.createStat(mail, args[0], args[1]);
		try{
			Record record = getRecordDao().readByPrimaryKey(recordPk);
			stat.setRecord(record);
			getDao().save(stat);
			result.setResult(true);
			log.debug("saved with success");
		} catch(Exception e) {
			log.warn("Error while persisting "+stat,e);
		}
		return result;
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

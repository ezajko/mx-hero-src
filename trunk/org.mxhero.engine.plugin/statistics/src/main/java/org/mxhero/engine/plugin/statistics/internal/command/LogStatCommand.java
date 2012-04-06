package org.mxhero.engine.plugin.statistics.internal.command;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.command.NamedParameters;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.statistics.command.LogStat;
import org.mxhero.engine.plugin.statistics.internal.entity.Stat;
import org.mxhero.engine.plugin.statistics.internal.entity.Utils;
import org.mxhero.engine.plugin.statistics.internal.repository.RecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogStatCommand implements LogStat{

	private static Logger log = LoggerFactory.getLogger(LogStatCommand.class);
	
	private Utils utils;
	
	private RecordRepository repository;
	
	/**
	 * @see org.mxhero.engine.domain.mail.command.Command#exec(org.mxhero.engine.domain.mail.MimeMail, java.lang.String[])
	 */
	@Override
	public Result exec(MimeMail mail, NamedParameters parameters) {
		Result result = new Result();
		
		if (parameters==null || (parameters.get(LogStat.KEY)==null||
								parameters.get(LogStat.VALUE)==null) ){
			log.warn("wrong params");
			result.setAnError(true);
			result.setMessage("wrong params");
			return result;
		}
		Stat stat = utils.createStat(mail, parameters.get(LogStat.KEY).toString(), parameters.get(LogStat.VALUE).toString());
		try{
			getRepository().saveStat(stat);
			result.setConditionTrue(true);
		} catch(Exception e) {
			log.warn("Error while persisting "+stat,e);
		}
		return result;
	}

	public RecordRepository getRepository() {
		return repository;
	}

	public void setRepository(RecordRepository repository) {
		this.repository = repository;
	}

	public Utils getUtils() {
		return utils;
	}

	public void setUtils(Utils utils) {
		this.utils = utils;
	}

}

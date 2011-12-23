package org.mxhero.engine.plugin.statistics.internal.command;

import org.mxhero.engine.commons.mail.MimeMail;
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
	public Result exec(MimeMail mail, String... args) {
		Result result = new Result();
		result.setResult(false);
		
		if (args==null || args.length!=2 || args[0]==null || args[0].isEmpty() ){
			log.warn("wrong params");
			return result;
		}

		Stat stat = utils.createStat(mail, args[0], (args[1]==null)?"":args[1]);
		try{
			getRepository().saveStat(stat);
			result.setResult(true);
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

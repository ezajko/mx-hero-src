package org.mxhero.engine.plugin.statistics.internal.service;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.statistic.LogStat;
import org.mxhero.engine.plugin.statistics.internal.entity.Utils;
import org.mxhero.engine.plugin.statistics.internal.repository.RecordRepository;

public class LogStatService implements LogStat{

	private RecordRepository repository;
	
	private Utils utils;
	
	@Override
	public void log(MimeMail mail, String key, String value) {
		repository.saveStat(utils.createStat(mail, key, value));
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

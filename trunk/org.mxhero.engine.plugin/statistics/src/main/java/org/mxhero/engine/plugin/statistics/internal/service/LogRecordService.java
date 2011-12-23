package org.mxhero.engine.plugin.statistics.internal.service;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.statistic.LogRecord;
import org.mxhero.engine.plugin.statistics.internal.entity.Utils;
import org.mxhero.engine.plugin.statistics.internal.repository.RecordRepository;

public class LogRecordService implements LogRecord{

	private RecordRepository repository;
	
	private Utils utils;
	
	@Override
	public void log(MimeMail mail) {
		repository.saveRecord(utils.createRecord(mail));
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

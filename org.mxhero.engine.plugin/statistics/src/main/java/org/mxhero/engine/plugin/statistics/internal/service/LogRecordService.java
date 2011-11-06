package org.mxhero.engine.plugin.statistics.internal.service;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.statistic.LogRecord;
import org.mxhero.engine.plugin.statistics.internal.entity.Utils;
import org.mxhero.engine.plugin.statistics.internal.repository.RecordRepository;

public class LogRecordService implements LogRecord{

	private RecordRepository repository;
	
	@Override
	public void log(MimeMail mail) {
		repository.saveRecord(Utils.createRecord(mail));
	}

	public RecordRepository getRepository() {
		return repository;
	}

	public void setRepository(RecordRepository repository) {
		this.repository = repository;
	}

}

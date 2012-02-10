package org.mxhero.engine.plugin.threadlight.internal.connector;

import org.mxhero.engine.commons.connector.InputServiceFilter;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.plugin.threadlight.internal.service.ThreadRowService;

public class ThreadLightFilter implements InputServiceFilter{

	private ThreadRowService threadRowService;
	
	@Override
	public void dofilter(MimeMail mail) {
		threadRowService.reply(mail);
	}

	public ThreadRowService getThreadRowService() {
		return threadRowService;
	}

	public void setThreadRowService(ThreadRowService threadRowService) {
		this.threadRowService = threadRowService;
	}

}

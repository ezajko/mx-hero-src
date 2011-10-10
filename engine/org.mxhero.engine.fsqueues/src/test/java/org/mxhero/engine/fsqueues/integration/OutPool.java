package org.mxhero.engine.fsqueues.integration;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.domain.queue.MimeMailQueueService;
import org.mxhero.engine.domain.queue.QueueTaskPool;

public class OutPool extends QueueTaskPool{

	private MimeMailQueueService queueService;
	
	public OutPool(MimeMailQueueService queue) {
		super(RulePhase.OUT, queue);
		this.queueService=queue;
	}

	@Override
	protected Runnable createTask(MimeMail object) {
		final MimeMail mail = object;
		return new Runnable() {
			
			public void run() {
				for(int i=0;i<10;i++)
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				queueService.unstore(mail);
			}
		};
	}

	@Override
	protected void init() {
	}

	@Override
	protected void clean() {
	}

}

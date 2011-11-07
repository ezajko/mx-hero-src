package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.session;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.MailSessionSate;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.Message;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DefaultMailSessionState implements MailSessionSate {

	private Map<String, Boolean> session;
	private ReentrantLock lock;

	@Autowired
	@Qualifier(value = "jdbcRepository")
	private AttachmentRepository repository;

	public DefaultMailSessionState() {
		lock = new ReentrantLock();
		session = new HashMap<String, Boolean>();
	}

	@Override
	public void finishProcessing(Message mail) {
		try {
			if (lock.tryLock(5, TimeUnit.SECONDS)) {
				try {
					session.remove(mail.getMessagePlatformId());
				} finally {
					lock.unlock();
				}
			} else {
				mail.requeueMessage();
			}
		} catch (InterruptedException e) {
		}
	}

	@Override
	public boolean hasBeenProcessed(Message mail) {
		boolean hasBeen = repository.hasBeenProcessed(mail);
		Boolean processing = isBeenProcessing(mail);
		if (hasBeen && processing) {
			finishProcessing(mail);
		}
		return hasBeen;
	}

	@Override
	public boolean isBeenProcessing(Message mail) {
		Boolean processing = Boolean.FALSE;
		try {
			if (lock.tryLock(5, TimeUnit.SECONDS)) {
				try {
					processing = session.get(mail.getMessagePlatformId());
				} finally {
					lock.unlock();
				}
			} else {
				mail.requeueMessage();
			}
		} catch (InterruptedException e) {
		}
		return processing != null && processing;
	}

	@Override
	public void startProcessing(Message mail) {
		try {
			if (lock.tryLock(5, TimeUnit.SECONDS)) {
				try {
					session.put(mail.getMessagePlatformId(), Boolean.TRUE);
				} finally {
					lock.unlock();
				}
			} else {
				mail.requeueMessage();
			}
		} catch (InterruptedException e) {
		}
	}

}

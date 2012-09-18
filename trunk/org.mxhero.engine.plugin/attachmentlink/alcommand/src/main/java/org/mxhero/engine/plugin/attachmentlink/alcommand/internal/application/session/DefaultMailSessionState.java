/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

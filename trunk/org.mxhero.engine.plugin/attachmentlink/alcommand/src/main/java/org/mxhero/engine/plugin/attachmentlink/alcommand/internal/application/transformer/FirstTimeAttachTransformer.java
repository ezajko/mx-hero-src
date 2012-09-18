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

package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.transformer;

import org.apache.log4j.Logger;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.AttachmentTransformer;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.Converter;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application.MailSessionSate;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service(value = "firstAttach")
public class FirstTimeAttachTransformer implements AttachmentTransformer {
	
	private static Logger logger = Logger.getLogger(FirstTimeAttachTransformer.class);

	@Autowired
	private MailSessionSate session;
	
	@Autowired
	@Qualifier(value = "alreadyAttach")
	private AttachmentTransformer next;
	
	@Autowired
	private Converter converter;
	
	public FirstTimeAttachTransformer() {
	}

	@Override
	public void transformAttachments(Message mail) {
		if(session.hasBeenProcessed(mail)){
			logger.debug("Message was processed. Getting attachments to transform");
			next.transformAttachments(mail);
		}else{
			logger.debug("First Time processing message. Processing...");
			converter.transform(mail);
		}
	}

}

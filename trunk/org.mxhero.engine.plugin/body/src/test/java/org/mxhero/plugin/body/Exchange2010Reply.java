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

package org.mxhero.plugin.body;

import java.io.IOException;

import javax.mail.MessagingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.plugin.body.command.AppendCurrentCommand;
import org.mxhero.engine.plugin.body.command.AppendCurrentParameters;
import org.mxhero.engine.plugin.body.command.ReadCurrentCommand;
import org.mxhero.engine.plugin.body.command.ReadCurrentResult;
import org.mxhero.engine.plugin.body.internal.search.MailBodyParts;
import org.mxhero.engine.plugin.body.internal.search.SearchMailBodyParts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-bundle-context.xml")
public class Exchange2010Reply {

	private static Logger log = LoggerFactory.getLogger(GmailReply.class);

	@Autowired
	private AppendCurrentCommand append;
	@Autowired
	private ReadCurrentCommand read;

	private MimeMail readGmail() throws MessagingException {
		return new MimeMail("sender@example.com", "recipient@example.com", this
				.getClass().getClassLoader()
				.getResourceAsStream("outlook2010reply.eml"), "service");
	}

	@Test
	public void readTxt() throws MessagingException {
		MimeMail mail = readGmail();

		ReadCurrentResult result = (ReadCurrentResult) read.exec(mail, null);

		log.debug(result.getPlainText());
		log.debug(result.getHtmlText());
		log.debug(result.getHtmlAsPlainText());
	}

	@Test
	public void insertTxt() throws MessagingException, IOException {
		MimeMail mail = readGmail();

		append.exec(mail, new AppendCurrentParameters("PLAIN TEXT HERE",
				"<b>HTML TEXT HERE</b>"));
		MailBodyParts parts = SearchMailBodyParts.search(mail.getMessage());

		log.debug(parts.getPlain().getText());
		log.debug(parts.getHtml().getText());

	}
}

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
public class Exchange2010Forward {

	private static Logger log = LoggerFactory.getLogger(GmailReply.class);

	@Autowired
	private AppendCurrentCommand append;
	@Autowired
	private ReadCurrentCommand read;

	private MimeMail readGmail() throws MessagingException {
		return new MimeMail("sender@example.com", "recipient@example.com", this
				.getClass().getClassLoader()
				.getResourceAsStream("outlook2010forward.eml"), "service");
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

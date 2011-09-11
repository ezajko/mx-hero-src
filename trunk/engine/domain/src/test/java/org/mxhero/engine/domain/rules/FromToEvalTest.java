package org.mxhero.engine.domain.rules;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;
import org.mxhero.engine.domain.mail.business.Domain;
import org.mxhero.engine.domain.mail.business.InitialData;
import org.mxhero.engine.domain.mail.business.Mail;
import org.mxhero.engine.domain.mail.business.User;

public class FromToEvalTest {

	@Test
	public void test(){
		User sender = new User();
		sender.setMail("mmarmol@mxhero.com");
		sender.setAliases(new ArrayList<String>());
		sender.getAliases().add("mmarmol@mxhero.com");
		Domain senderDomain = new Domain();
		senderDomain.setId("mxhero.com");
		senderDomain.setAliases(new ArrayList<String>());
		senderDomain.getAliases().add("mxhero.com");
		sender.setDomain(senderDomain);
		
		User recipient = new User();
		recipient.setMail("teste.2@conbras.com");
		recipient.setAliases(new ArrayList<String>());
		recipient.getAliases().add("teste.2@conbrasengenharia.com.br");
		recipient.getAliases().add("teste.2@conbras.com");
		recipient.setManaged(true);
		Domain recipientDomain = new Domain();
		recipientDomain.setId("conbras.com");
		recipientDomain.setAliases(new ArrayList<String>());
		recipientDomain.getAliases().add("conbras.com");
		recipientDomain.getAliases().add("conbrasengenharia.com.br");
		recipient.setDomain(recipientDomain);
               
		InitialData initialData = new InitialData();
		initialData.setFromSender(sender);
		initialData.setSender(sender);
		initialData.setRecipient(recipient);
		
		Mail mail = new Mail();
		mail.setInitialData(initialData);
		
		RuleDirectionDummy from = new RuleDirectionDummy();
		from.setDirectionType("domain");
		from.setFreeValue("mxhero.com");
		RuleDirectionDummy to = new RuleDirectionDummy();
		to.setDirectionType("domain");
		to.setFreeValue("conbras.com");
		to.setDomain("conbras.com");

		FromToEval eval = new FromToEval(from, to, false);
		Assert.assertTrue(eval.eval(mail));
	}
	
}

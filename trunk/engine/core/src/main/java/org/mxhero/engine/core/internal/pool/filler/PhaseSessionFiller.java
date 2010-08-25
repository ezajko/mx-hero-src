package org.mxhero.engine.core.internal.pool.filler;

import org.drools.runtime.StatefulKnowledgeSession;
import org.mxhero.engine.core.mail.AttachmentsVO;
import org.mxhero.engine.core.mail.BodyVO;
import org.mxhero.engine.core.mail.HeadersVO;
import org.mxhero.engine.core.mail.InitialDataVO;
import org.mxhero.engine.core.mail.MailVO;
import org.mxhero.engine.core.mail.RecipientsVO;
import org.mxhero.engine.core.mail.SenderVO;
import org.mxhero.engine.core.mail.SubjectVO;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.Domain;
import org.mxhero.engine.domain.mail.business.DomainList;
import org.mxhero.engine.domain.mail.business.Group;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.domain.mail.business.User;
import org.mxhero.engine.domain.mail.business.UserList;
import org.mxhero.engine.domain.mail.finders.DomainFinder;
import org.mxhero.engine.domain.mail.finders.UserFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class add the business objects to the session.
 * 
 * @author mmarmol
 */
public class PhaseSessionFiller implements SessionFiller {

	private static Logger log = LoggerFactory
			.getLogger(PhaseSessionFiller.class);

	private static final char DIV_CHAR = '@';

	/**
	 * @see org.mxhero.engine.core.internal.pool.filler.SessionFiller#fill(org.drools.runtime.StatefulKnowledgeSession, org.mxhero.engine.domain.mail.finders.UserFinder, org.mxhero.engine.domain.mail.finders.DomainFinder, org.mxhero.engine.domain.mail.MimeMail)
	 */
	@Override
	public String fill(StatefulKnowledgeSession ksession,
			UserFinder userFinder, DomainFinder domainFinder, MimeMail mail) {
		Domain domain = null;
		User user = null;
		String userMail = null;
		String domainAgendaGroup = null;

		if (mail != null) {
			if (mail.getPhase().equals(RulePhase.SEND)) {
				userMail = mail.getInitialSender();
			} else if (mail.getPhase().equals(RulePhase.RECEIVE)) {
				userMail = mail.getRecipient();
			}
		}

		if (userMail != null) {
			if (domainFinder != null) {

				String domainId = userMail
						.substring(userMail.indexOf(DIV_CHAR) + 1);
				domain = domainFinder.getDomain(domainId);
				if (domain != null) {
					log.debug("domain found " + domain);
					ksession.insert(domain);
					if(domain.getGroups()!=null){
						for (Group group : domain.getGroups()) {
							ksession.insert(group);
						}
					}
					if(domain.getLists()!=null){
						for (DomainList domainList : domain.getLists()) {
							ksession.insert(domainList);
						}
					}
					if (userFinder != null) {
						user = userFinder.getUser(userMail, domainId);
						if (user != null) {
							log.debug("user found " + user);
							ksession.insert(user);
							if(user.getLists()!=null){
								for (UserList userList : user.getLists()) {
									ksession.insert(userList);
								}
							}
						}
					}
					/* add domain of the recipient agenda group */
					domainAgendaGroup = domain.getId();
				}
			}

			if (domainAgendaGroup == null) {
				domainAgendaGroup = userMail.substring(userMail
						.indexOf(DIV_CHAR) + 1);
			}
		}

		ksession.insert(new MailVO(mail));
		ksession.insert(new InitialDataVO(mail));
		ksession.insert(new HeadersVO(mail));
		ksession.insert(new SenderVO(mail));
		ksession.insert(new SubjectVO(mail));
		ksession.insert(new RecipientsVO(mail));
		ksession.insert(new BodyVO(mail));
		ksession.insert(new AttachmentsVO(mail));

		if (log.isDebugEnabled()) {
			log.debug("All facts are:");
			for (Object obj : ksession.getObjects()) {
				log.debug(obj.toString());
			}
		}

		return domainAgendaGroup;
	}

}

package org.mxhero.engine.commons.mail.api;

import java.util.List;
import java.util.Map;

import org.mxhero.engine.commons.domain.User;
import org.mxhero.engine.commons.mail.command.NamedParameters;
import org.mxhero.engine.commons.mail.command.Result;

/**
 * Represents data of the mail so it can be used inside rules.
 * @author mmarmol
 */
public interface Mail {
	
	/**
	 * Current state of the mail object.
	 * @author mxhero
	 */
	public enum Status {
		deliver,drop,requeue,redirect;
	}
	
	/**
	 * Current phase of the email been processed by the engine.
	 * @author mxhero
	 */
	public enum Phase {
		send,receive,out;
	}
	
	/**
	 * 
	 * @author mxhero
	 */
	public enum Flow {
		in,out,both,none;
	}
	
	/**
	 * @return
	 */
	public String getId();
	
	/**
	 * @param commandServiceId
	 * @param params
	 * @return
	 */
	public Result cmd(String commandServiceId, NamedParameters params);
	
	/**
	 * @return
	 */
	public Phase getPhase();
	
	/**
	 * @return
	 */
	public Status getStatus();
	
	/**
	 * @return
	 */
	public Flow getFlow();

	/**
	 * @return
	 */
	public String getStatusReason();
	
	/**
	 * @return
	 */
	public long getInitialSize();
	
	
	public long getSize();
	
	/**
	 * @return
	 */
	public User getSender();
	
	/**
	 * @return
	 */
	public User getFromSender();
	
	/**
	 * @return
	 */
	public User getRecipient();
	
	
	/**
	 * @return
	 */
	public List<User> getRecipientsInHeaders();

	/**
	 * @return
	 */
	public Map<String, String> getProperties();

	/**
	 * @param reason
	 * @return
	 */
	public abstract boolean drop(String reason);
	
	/**
	 * @param reason
	 * @return
	 */
	public abstract boolean redirect(String reason);

	/**
	 * @return
	 */
	public Headers getHeaders();

	/**
	 * @return
	 */
	public abstract String getSubject();

	/**
	 * @param subject
	 */
	public abstract void setSubject(String subject);

	/**
	 * @return
	 */
	public Recipients getRecipients();

	/**
	 * @return
	 */
	public Body getBody();

	/**
	 * @return
	 */
	public Attachments getAttachments();
	
}

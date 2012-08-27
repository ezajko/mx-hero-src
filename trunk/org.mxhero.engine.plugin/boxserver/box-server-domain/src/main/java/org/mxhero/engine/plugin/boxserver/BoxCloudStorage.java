package org.mxhero.engine.plugin.boxserver;

import org.mxhero.engine.plugin.boxserver.service.ApplicationBox;
import org.mxhero.engine.plugin.boxserver.service.TicketBox;
import org.mxhero.engine.plugin.boxserver.service.UserBox;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * The Class BoxCloudStorage.
 */
public class BoxCloudStorage implements BeanFactoryAware{
	
	/** The bean factory. */
	private BeanFactory beanFactory;
	
	/**
	 * Creates the account.
	 *
	 * @param email the email
	 * @return the user box
	 */
	public UserBox createAccount(String email){
		UserBox user = getUserBoxInstance(email);
		user.createAccount();
		return user;
	}

	
	/**
	 * Gets the user box instance.
	 *
	 * @param email the email
	 * @return the user box instance
	 */
	private UserBox getUserBoxInstance(String email){
		return (UserBox) this.beanFactory.getBean("userBox", email);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}


	/**
	 * Gets the ticket.
	 *
	 * @return the ticket
	 */
	public String getTicket() {
		return getTicketBox().getNewTicket();
	}


	/**
	 * Gets the ticket box.
	 *
	 * @return the ticket box
	 */
	private TicketBox getTicketBox() {
		return beanFactory.getBean(TicketBox.class);
	}


	/**
	 * Register token.
	 *
	 * @param ticket the ticket
	 * @param token the token
	 * @param appKey 
	 * @return 
	 */
	public String registerToken(String ticket, String token, String appKey) {
		return getTicketBox().registerToken(ticket,token, appKey);
	}


	/**
	 * Gets the token.
	 *
	 * @param email the email
	 * @return the token
	 */
	public UserBox getToken(String email) {
		UserBox userBoxInstance = getUserBoxInstance(email);
		userBoxInstance.retrieveToken();
		return userBoxInstance;
	}


	/**
	 * Authenticate application.
	 *
	 * @param appKey the app key
	 * @return true, if successful
	 */
	public boolean authenticateApplication(String appKey) {
		ApplicationBox app = getAppBox();
		return app.authenticate(appKey);
	}


	/**
	 * Gets the app box.
	 *
	 * @return the app box
	 */
	private ApplicationBox getAppBox() {
		return this.beanFactory.getBean(ApplicationBox.class);
	}


	/**
	 * Creates the application key.
	 *
	 * @param name the name
	 * @return the string
	 */
	public String createApplicationKey(String name) {
		return getAppBox().getAppKey(name);
	}


	/**
	 * Gets the api box key.
	 *
	 * @return the api box key
	 */
	public String getApiBoxKey() {
		return getAppBox().getApiBoxKey();
	}

}

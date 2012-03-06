package org.mxhero.console.backend.ws.handler;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

public class SecurityHandler implements SOAPHandler<SOAPMessageContext> {

	private String userName = "admin";

	private String password = "password";

	private static Logger log = Logger.getLogger(SecurityHandler.class);

	/** The Constant USERNAME_TOKEN_STRING. */
	private static final String USERNAME_TOKEN_STRING = "UsernameToken";

	/** The Constant USERNAME_STRING. */
	private static final String USERNAME_STRING = "Username";

	/** The Constant PASSWORD_STRING. */
	private static final String PASSWORD_STRING = "Password";

	public Set<QName> getHeaders() {
		return null;
	}

	public void close(MessageContext context) {
	}

	public boolean handleFault(SOAPMessageContext context) {

		logToSystemOut(context);
		return true;
	}

	public boolean handleMessage(SOAPMessageContext context) {
		Boolean outboundProperty = (Boolean) context
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		boolean isSoapRequestHandle = false;

		if (outboundProperty.booleanValue()) {
			isSoapRequestHandle = true;
		} else {
			try {

				SOAPMessage message = context.getMessage();
				SOAPPart sp = message.getSOAPPart();

				SOAPEnvelope envelope = sp.getEnvelope();

				SOAPHeader sh = envelope.getHeader();
				isSoapRequestHandle = processSOAPHeader(sh);

				 message.writeTo(System.out);
				 System.out.println("");

				if (!isSoapRequestHandle) {

					SOAPElement errorMessage = sh.addChildElement(
							"errorMessage", "error",
							"http://ws.backend.console.mxhero.org/api/error");
					SOAPElement error = errorMessage.addChildElement("error");
					error.addTextNode("Authentication Failed");

				}
			} catch (Exception e) {
				log.error(e);
			}
		}

		logToSystemOut(context);
		return isSoapRequestHandle;
	}

	private void logToSystemOut(SOAPMessageContext smc) {
		Boolean outboundProperty = (Boolean) smc
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if (outboundProperty.booleanValue()) {
			log.debug("\nOutgoing message:");
		} else {
			log.debug("\nIncoming message:");
		}

		SOAPMessage message = smc.getMessage();
		try {
			log.debug(handleRequestAndResponse(message));
		} catch (Exception e) {
			log.error("Exception in handler: " + e);
		}
	}

	private String handleRequestAndResponse(SOAPMessage msg) {
		ByteArrayOutputStream obj = new ByteArrayOutputStream();
		try {
			msg.writeTo(obj);
			return obj.toString();
		} catch (Exception ex) {
			obj = null;
			ex.printStackTrace();
		}
		return "";
	}

	private boolean processSOAPHeader(SOAPHeader sh) {
		boolean authenticated = false;

		// look for authentication header element inside the HEADER block
		Iterator childElems = sh.getChildElements();
		SOAPElement child = extractUserNameInfo(childElems);
		if (child != null) {
			// call method to perform authentication
			authenticated = authenticateRequest(child);
		}
		return authenticated;
	}

	private SOAPElement extractUserNameInfo(Iterator childElems) {
		SOAPElement child = null;
		Name sName;
		// iterate through child elements
		while (childElems.hasNext()) {
			Object elem = childElems.next();

			if (elem instanceof SOAPElement) {
				// Get child element and its name
				child = (SOAPElement) elem;
				sName = child.getElementName();
				// Check whether there is a UserNameToken element
				if (!USERNAME_TOKEN_STRING.equalsIgnoreCase(sName
						.getLocalName())) {

					if (child.getChildElements().hasNext()) { // TODO check
																// logic
						return extractUserNameInfo(child.getChildElements());
					}
				}
			}
		}

		return child;
	}

	private boolean authenticateRequest(SOAPElement element) {

		boolean authenticated = false;

		// variable for user name and password
		String userName = null;
		String password = null;
		Name sName;

		// get an iterator on child elements of SOAP element
		Iterator childElems = element.getChildElements();

		SOAPElement child;
		// loop through child elements

		while (childElems.hasNext()) {
			// get next child element
			Object elem = childElems.next();

			if (elem instanceof SOAPElement) {
				child = (SOAPElement) elem;

				// get the name of SOAP element
				sName = child.getElementName();

				// get the value of username element
				if (USERNAME_STRING.equalsIgnoreCase(sName.getLocalName())) {
					userName = child.getValue();
				} else if (PASSWORD_STRING.equalsIgnoreCase(sName
						.getLocalName())) {
					// get the value of password element
					password = child.getValue();
				}

				if (userName != null && password != null) {

					// ClientLoginModule.login("WEBSERVICE" + "^" + userName,
					// password);
					// return true;

					authenticated = getUserAuth(userName, password);

					break;
				}

			}
		}

		if (userName == null || password == null) {
			log.warn("Username or password is empty. userName : [" + userName
					+ "], password : [" + password + "]");
		}

		return authenticated;

	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private boolean getUserAuth(String username, String password) {

		if (this.userName.equalsIgnoreCase(username)
				&& this.password.equals(password)) {
			return true;
		}

		return false;
	}
}
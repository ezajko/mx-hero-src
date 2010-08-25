package org.mxhero.engine.plugin.spamd.internal.scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class used to send a mail to the spam daemon.
 * @author mmarmol
 */
public class SpamdScanner {

	private static Logger log = LoggerFactory.getLogger(SpamdScanner.class);
	
    /**
     * The mail attribute under which the status get stored
     */
    public static final String STATUS_MAIL_ATTRIBUTE_NAME = "X-Spam-Status";
    public static final String FLAG_MAIL_ATTRIBUTE_NAME = "X-Spam-Flag";
    
    public static final String DEFAULT_HOSTNAME = "localhost";
    public static final int DEFAULT_PORT = 783;
    
    private String spamdHost;

    private int spamdPort;
    
    private String hits = "?";

    private String required = "?";

    private Map<String,String> headers = new HashMap<String,String>();

    /**
     * Init the spamassassin invoker
     * 
     * @param spamdHost The host on which spamd runs
     * @param spamdPort The port on which spamd listen
     */
    public SpamdScanner(String spamdHost, int spamdPort) {
        if(spamdHost==null || spamdHost.isEmpty()){
        	log.warn("wrong host name format, using default");
        	this.spamdHost = DEFAULT_HOSTNAME;
        } else {
        	this.spamdHost = spamdHost;
        }
    	if(spamdPort<1){
    		log.warn("wrong port format, using default");
    		this.spamdPort = DEFAULT_PORT;
    	} else {
            this.spamdPort = spamdPort;    		
    	}
    }

    /**
     * Scan a MimeMessage for spam by passing it to spamd.
     * 
     * @param message
     *            The MimeMessage to scan
     * @return true if spam otherwise false
     * @throws MessagingException 
     * @throws MessagingException
     *             if an error on scanning is detected
     */
    public boolean scan(MimeMessage message) throws MessagingException {
        Socket socket = null;
        OutputStream out = null;
        BufferedReader in = null;
        log.debug("scanning message"+message.getMessageID());
        try {
            socket = new Socket(spamdHost, spamdPort);

            out = socket.getOutputStream();
            in = new BufferedReader(new InputStreamReader(socket
                    .getInputStream()));
            out.write("CHECK SPAMC/1.2\r\n\r\n".getBytes());

            // pass the message to spamd
            message.writeTo(out);
            out.flush();
            socket.shutdownOutput();
            String s = null;
            while ((s = in.readLine()) != null) {
                if (s.startsWith("Spam:")) {
                    StringTokenizer t = new StringTokenizer(s, " ");
                    boolean spam;
                    try {
                        t.nextToken();
                        spam = Boolean.valueOf(t.nextToken()).booleanValue();
                    } catch (Exception e) {
                        // On exception return false
                        return false;
                    }
                    t.nextToken();
                    hits = t.nextToken();
                    t.nextToken();
                    required = t.nextToken();

                    if (spam) {
                        // message was spam
                    	log.debug("message is spam"+message.getMessageID());
                    	headers.put(FLAG_MAIL_ATTRIBUTE_NAME,"Yes");
                    	headers.put(STATUS_MAIL_ATTRIBUTE_NAME,
                                new StringBuffer("Yes, hits=").append(hits)
                                        .append(" required=").append(required)
                                        .toString());

                        // spam detected
                        return true;
                    } else {
                        // add headers
                    	log.debug("message is NOT spam"+message.getMessageID());
                    	headers.put(FLAG_MAIL_ATTRIBUTE_NAME,"No");
                    	headers.put(STATUS_MAIL_ATTRIBUTE_NAME,
                                new StringBuffer("No, hits=").append(hits)
                                        .append(" required=").append(required)
                                        .toString());

                        return false;
                    }
                }
            }
        } catch (UnknownHostException e1) {
        	log.warn("Error communicating with spamd. Unknown host: "
                + spamdHost);
        } catch (IOException e1) {
        	log.warn("Error communicating with spamd on "
                    + spamdHost + ":" + spamdPort + " Exception: " + e1);
        } catch (MessagingException e1) {
        	log.warn("Error communicating with spamd on "
                    + spamdHost + ":" + spamdPort + " Exception: " + e1);
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (Exception e) {
            	log.warn("error",e);
            }

        }
		return false;
    }

    /**
     * Return the hits which was returned by spamd
     * 
     * @return hits The hits which was detected
     */
    public String getHits() {
        return hits;
    }

    /**
     * Return the required hits
     * 
     * @return required The required hits before a message is handled as spam
     */
    public String getRequiredHits() {
        return required;
    }

    /**
     * Return the headers as attributes which spamd generates
     * 
     * @return headers Map of headers to add as attributes
     */
    public Map<String,String> getHeadersAsAttribute() {
        return headers;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Spamd [spamdHost=" + spamdHost + ", spamdPort=" + spamdPort
				+ ", hits=" + hits + ", required=" + required + ", headers="
				+ headers + "]";
	}

}

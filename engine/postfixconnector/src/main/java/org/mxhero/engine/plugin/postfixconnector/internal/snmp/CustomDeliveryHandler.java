package org.mxhero.engine.plugin.postfixconnector.internal.snmp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;

import org.mailster.smtp.api.RejectException;
import org.mailster.smtp.api.TooMuchDataException;
import org.mailster.smtp.api.handler.AbstractDeliveryHandler;
import org.mailster.smtp.api.handler.Delivery;
import org.mailster.smtp.api.handler.DeliveryContext;
import org.mailster.smtp.api.listener.MessageListener;
import org.mailster.smtp.auth.AuthenticationHandler;
import org.mailster.smtp.util.SharedStreamUtils;
import org.mxhero.engine.plugin.postfixconnector.internal.service.PostfixConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom implementation so we can send only one email with all the recipients
 * the recipients.
 * 
 * @author mmarmol
 */
public class CustomDeliveryHandler extends AbstractDeliveryHandler {

	private static final int REJECT_CODE = 553;
	private static final int BUFF_SIZE = 16384;

	public static final String SPLIT_CHAR = ";";

	private static Logger log = LoggerFactory
			.getLogger(CustomDeliveryHandler.class);

	private List<Delivery> deliveries = new ArrayList<Delivery>();
	private String from;

	private long maxSizeInBytes=1024*1024;
	
	/**
	 * Basic constructor.
	 * @param ctx
	 * @param authHandler
	 */
	public CustomDeliveryHandler(DeliveryContext ctx,
			AuthenticationHandler authHandler) {
		super(ctx, authHandler);
	}

	/**
	 * {@inheritDoc}
	 */
	public void from(String from) throws RejectException {
		this.from = from;
	}

	/**
	 * {@inheritDoc}
	 */
	public void recipient(String recipient) throws RejectException {
		boolean addedListener = false;

		for (MessageListener listener : getListeners()) {
			if (listener.accept(getSessionContext(), this.from, recipient)) {
				this.deliveries.add(new Delivery(listener, recipient));
				addedListener = true;
			}
		}

		if (!addedListener) {
			throw new RejectException(REJECT_CODE, "<" + recipient
					+ "> address unknown.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void resetMessageState() {
		this.deliveries.clear();
	}

	/**
	 * Implementation of the data receiving portion of things. By default
	 * deliver a copy of the stream to each recipient of the message(the first
	 * recipient is provided the original stream to save memory space). If you
	 * would like to change this behavior, then you should implement the
	 * MessageHandler interface yourself.
	 */
	@Override
	public void data(InputStream data) throws TooMuchDataException, IOException {
		boolean useCopy = false;

		if (log.isTraceEnabled()) {
			Charset charset = getDeliveryContext().getSMTPServerConfig()
					.getCharset();
			InputStream in = SharedStreamUtils.getPrivateInputStream(useCopy,
					data);
			byte[] buf = new byte[BUFF_SIZE];

			try {
				CharsetDecoder decoder = charset.newDecoder();
				int len = 0;
				while ((len = in.read(buf)) >= 0) {
					log.trace(decoder.decode(ByteBuffer.wrap(buf, 0, len))
							.toString());
				}
			} catch (IOException ioex) {
				log.trace("Mail data logging failed", ioex);
			}
			useCopy = true;
		}

		if(PostfixConnector.getCurrentInstanceValue(PostfixConnector.MESSAGE_MAX_SIZE)!=null){
			try{
				maxSizeInBytes=Long.parseLong(PostfixConnector.getCurrentInstanceValue(PostfixConnector.MESSAGE_MAX_SIZE));
			}catch(Exception e){
				log.warn("wrong param "+PostfixConnector.MESSAGE_MAX_SIZE);
			}
		}
		//check for max size allowed for emails
		if(maxSizeInBytes>1042){
			InputStream in = SharedStreamUtils.getPrivateInputStream(true,
					data);
			byte[] buf = new byte[BUFF_SIZE];
			int len = 0;
			long total=0;
			while ((len = in.read(buf)) >= 0) {
				total=total+len;
			}
			if(total>=maxSizeInBytes){
				throw new TooMuchDataException();
			}
		}
		
		// Prevent concurrent modifications
		List<Delivery> list = new ArrayList<Delivery>(this.deliveries);

		for (Delivery delivery : list)
		{				
		    delivery.getListener().deliver(getSessionContext(), this.from, 
		    		delivery.getRecipient(), SharedStreamUtils.getPrivateInputStream(useCopy, data));
		    
		    // Use a stream copy on second iteration if not the case yet
		    useCopy = true;
		}

	}

	public long getMaxSizeInBytes() {
		return maxSizeInBytes;
	}

	public void setMaxSizeInBytes(long maxSizeInBytes) {
		this.maxSizeInBytes = maxSizeInBytes;
	}

}

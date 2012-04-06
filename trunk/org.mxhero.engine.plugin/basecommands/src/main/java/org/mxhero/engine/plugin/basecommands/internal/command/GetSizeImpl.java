package org.mxhero.engine.plugin.basecommands.internal.command;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.command.NamedParameters;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.basecommands.command.GetSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the GetSize interface. Returns the real size of an email. Takes
 * only one parameter. That parameter is the format that is used to the size
 * value returned. The values are b, k and m. (bytes, kbytes, megabytes).
 * 
 * @author mmarmol
 */
public class GetSizeImpl implements GetSize {

	private static Logger log = LoggerFactory.getLogger(GetSizeImpl.class);

	public static final String BYTES = "b";
	public static final String KBYTES = "k";
	private static final long KBYTES_BYTES = 1024;
	public static final String MEGABYTES = "m";
	private static final long MEGABYTES_BYTES = 1024 * 1024;
	private static final long WRONG_SIZE = -1;
	private static final long CRLF_SIZE = 2;

	/**
	 * @see org.mxhero.engine.domain.mail.command.Command#exec(org.mxhero.engine.domain.mail.MimeMail,
	 *      java.lang.String[])
	 */
	@Override
	public Result exec(MimeMail mail, NamedParameters parameters) {
		long size = WRONG_SIZE;
		Result result = new Result();
		
		String formatBytes = parameters.get(GetSize.FORMAT);
		if(formatBytes==null || (!formatBytes.equalsIgnoreCase(BYTES)&&
								!formatBytes.equalsIgnoreCase(KBYTES)&&
								!formatBytes.equalsIgnoreCase(MEGABYTES))){
			log.warn("format is incorrect should be b|B|k|K|m|M");
			result.setConditionTrue(false);
			result.setAnError(true);
			result.setMessage("format is incorrect should be b|B|k|K|m|M");
			return result;
		}

		size = calculateMessageSize(mail.getMessage());
		if (formatBytes == KBYTES) {
			size = size / KBYTES_BYTES;
		} else if (formatBytes == MEGABYTES) {
			size = size / MEGABYTES_BYTES;
		}

		if (size != WRONG_SIZE) {
			result.setConditionTrue(true);
			result.setAnError(false);
			result.setMessage(Long.toString(size) + formatBytes);
			result.setParameters(new NamedParameters("size", size));
		} else {			
			result.setConditionTrue(false);
			result.setAnError(true);
		}
		log.debug(result.toString());
		return result;
	}

	/**
	 * Used internally to know the size of the MimeMessage.
	 * 
	 * @param message
	 *            Mime message that we want to know the size.
	 * @return the size in bytes
	 */
	private long calculateMessageSize(MimeMessage message) {
		long size;

		try {
			size = message.getSize();
		} catch (MessagingException e1) {
			log.warn("error while trying to read message size");
			size = WRONG_SIZE;
		}
		if (size != WRONG_SIZE) {
			@SuppressWarnings("rawtypes")
			Enumeration e;
			try {
				e = message.getAllHeaderLines();
				if (e.hasMoreElements()) {
					size += 2;
				}
				while (e.hasMoreElements()) {
					// add 2 bytes for the CRLF
					size += ((String) e.nextElement()).length() + CRLF_SIZE;
				}
			} catch (MessagingException e1) {
				log.warn("error while trying to read message headers");
				size = WRONG_SIZE;
			}
		}

		if (size == -1) {
			SizeCalculatorOutputStream out = new SizeCalculatorOutputStream();
			try {
				message.writeTo(out);
			} catch (IOException e) {
				log.warn("error while trying to read stream", e);
				return -1;
			} catch (MessagingException e) {
				log.warn("error while trying to read stream", e);
				return -1;
			}
			size = out.getSize();
		}
		return size;
	}

	/**
	 * Slow method to calculate the exact size of a message!
	 * 
	 * @author mmarmol
	 */
	private static final class SizeCalculatorOutputStream extends OutputStream {
		private long size = 0;

		/**
		 * @see java.io.OutputStream#write(int)
		 */
		public void write(int arg0) throws IOException {
			size++;
		}

		/**
		 * @return return the amount of bytes written.
		 */
		public long getSize() {
			return size;
		}

		/**
		 * @see java.io.OutputStream#write(byte[], int, int)
		 */
		public void write(byte[] arg0, int arg1, int arg2) throws IOException {
			size += arg2;
		}

		/**
		 * @see java.io.OutputStream#write(byte[])
		 */
		public void write(byte[] arg0) throws IOException {
			size += arg0.length;
		}
	}
}

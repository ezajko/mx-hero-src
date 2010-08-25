package org.mxhero.engine.plugin.postfixconnector.internal.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Used to get bytes from inputstreams
 * @author mmarmol
 */
public abstract class StreamUtils {

	private static final int BUFFER_SIZE = 1024;
	
	/**
	 * Just private constructor
	 */
	private StreamUtils(){
	}
	
	/**
	 * 
	 * @param is
	 * @return array with all the bytes in the stream
	 * @throws IOException
	 */
	public static byte[] getBytes(InputStream is) throws IOException {
		int len;
		int size = BUFFER_SIZE;
		byte[] buf;

		if (is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte[size];
			is.read(buf, 0, size);
		} else {
			CustomByteArrayOutputStream bos = new CustomByteArrayOutputStream();
			buf = new byte[size];
			while ((len = is.read(buf, 0, size)) != -1){
				bos.write(buf, 0, len);
			}
			buf = bos.getBytes();
		}
		return buf;
	}
	
	/**
	 * @author mmarmol
	 *
	 */
	private static class CustomByteArrayOutputStream extends ByteArrayOutputStream{
		
		private byte[] getBytes(){
			return super.buf;
		}
		
	}
	
}

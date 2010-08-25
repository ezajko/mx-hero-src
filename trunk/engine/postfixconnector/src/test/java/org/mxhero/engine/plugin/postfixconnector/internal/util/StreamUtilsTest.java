package org.mxhero.engine.plugin.postfixconnector.internal.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;

import org.junit.Test;
import org.mxhero.engine.plugin.postfixconnector.internal.util.StreamUtils;

@SuppressWarnings("deprecation")
public class StreamUtilsTest {

	@Test
	public void testStreams() throws IOException{
		StreamUtils.getBytes(new ByteArrayInputStream("bytes".getBytes()));
		StreamUtils.getBytes(new StringBufferInputStream("bytes"));
	}
	
}

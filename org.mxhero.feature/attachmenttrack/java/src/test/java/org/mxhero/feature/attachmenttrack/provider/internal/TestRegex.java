package org.mxhero.feature.attachmenttrack.provider.internal;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.mail.internet.ParameterList;
import javax.mail.internet.ParseException;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.mxhero.engine.commons.util.HeaderUtils;

public class TestRegex {

	@Test
	public void testRegex() throws ParseException, UnsupportedEncodingException{
		System.out.println(Arrays.deepToString(HeaderUtils.parseParameters("\"attachmentTrack=\\\"1,2\\\";\"".replace("\\\"", "\""),"attachmentTrack")));
	}


}

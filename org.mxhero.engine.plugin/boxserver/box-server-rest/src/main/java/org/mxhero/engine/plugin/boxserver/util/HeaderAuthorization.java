package org.mxhero.engine.plugin.boxserver.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class HeaderAuthorization.
 */
public class HeaderAuthorization {
	
	/**
	 * Gets the app key from header.
	 *
	 * @param header the header
	 * @return the app key from header
	 */
	public static String getAppKeyFromHeader(String header){
		Pattern compile = Pattern.compile("mxHeroApi app_key=(.+)");
		Matcher matches = compile.matcher(header);
		String appKey = null;
		if(matches.find()){
			appKey = matches.group(1);
		}
		return appKey;
	}

}

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
		return getFromHeader("mxHeroApi app_key=(.+)", header);
	}

	/**
	 * Gets the box key from header.
	 *
	 * @param header the header
	 * @return the box key from header
	 */
	public static String getBoxKeyFromHeader(String header) {
		return getFromHeader("module_key=(.+)", header);
	}

	/**
	 * Gets the from header.
	 *
	 * @param regex the regex
	 * @param header the header
	 * @return the from header
	 */
	private static String getFromHeader(String regex, String header) {
		Pattern compile = Pattern.compile(regex);
		Matcher matches = compile.matcher(header);
		String appKey = null;
		if(matches.find()){
			appKey = matches.group(1);
		}
		return appKey;
	}

}

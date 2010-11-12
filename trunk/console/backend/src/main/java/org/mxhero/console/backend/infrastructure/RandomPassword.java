package org.mxhero.console.backend.infrastructure;

import java.util.Random;

public abstract class RandomPassword {
	private static final String charset = "0123456789abcdefghijklmnopqrstuvwxyz";

	public static String getRandomString(int length) {
		Random rand = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int pos = rand.nextInt(charset.length());
			sb.append(charset.charAt(pos));
		}
		return sb.toString();
	}
}

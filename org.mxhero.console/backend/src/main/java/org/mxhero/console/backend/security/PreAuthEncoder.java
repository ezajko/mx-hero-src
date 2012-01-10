package org.mxhero.console.backend.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;

public abstract class PreAuthEncoder {

	public static String encode(String loginName, String domain, String timestamp, String expires, String key) {
		return getHmac(loginName + "|" + domain + "|" + timestamp + "|"+ expires, key.getBytes());
	}

	private static String getHmac(String data, byte[] key) {
		try {
			ByteKey bk = new ByteKey(key);
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(bk);
			return toHex(mac.doFinal(data.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("fatal error", e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException("fatal error", e);
		}
	}

	static class ByteKey implements SecretKey {
		private byte[] mKey;

		ByteKey(byte[] key) {
			mKey = (byte[]) key.clone();
			;
		}

		public byte[] getEncoded() {
			return mKey;
		}

		public String getAlgorithm() {
			return "HmacSHA1";
		}

		public String getFormat() {
			return "RAW";
		}
	}

	public static String toHex(byte[] data) {
		StringBuilder sb = new StringBuilder(data.length * 2);
		for (int i = 0; i < data.length; i++) {
			sb.append(hex[(data[i] & 0xf0) >>> 4]);
			sb.append(hex[data[i] & 0x0f]);
		}
		return sb.toString();
	}

	private static final char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
}

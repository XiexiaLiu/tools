package com.futuredata.base.core.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptUtils {

	private static MessageDigest md5;
	private static final Logger logger = LoggerFactory.getLogger(EncryptUtils.class);

	private static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	private final static int MAX_ROUND_INT = 99999;
	static {
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException ex) {
			logger.error("", ex);
		}
	}

	public static String md5(String str, String charset) {
		StringBuilder sb = new StringBuilder();
		try {
			byte[] bb = md5.digest(str.getBytes(charset));
			for (byte b : bb) {
				sb.append(hexDigits[b >>> 4 & 0xf]);
				sb.append(hexDigits[b & 0xf]);
			}
		} catch (UnsupportedEncodingException ex) {
			logger.error("", ex);
		}

		return sb.toString();
	}

	public static String md5(String str) {
		return md5(str, "UTF-8").toUpperCase();
	}

	public static String getSalt() {
		int random = ThreadLocalRandom.current().nextInt(MAX_ROUND_INT);
		int index = random % hexDigits.length;
		return String.valueOf(random) + String.valueOf(hexDigits[index]);
	}

	public static String getPassword(String pwd, String salt) {
		return md5(pwd + salt);
	}

	public static void main(String[] args) {

	}
}

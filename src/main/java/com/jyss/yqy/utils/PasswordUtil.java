package com.jyss.yqy.utils;

import java.security.MessageDigest;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;

public class PasswordUtil {
	/**
	 * 生成含有随机盐的密码
	 */
	public static String generate(String password, String salt) {
		Random r = new Random();
		String pKey = "#" + "pUk3NYQ!YeG&G&N#U89qt7QXc7Az6ky%" + "#";
		password = md5Hex(password + pKey + salt);
		return password;
	}

	/**
	 * 校验密码是否正确
	 */
	public static boolean verify(String password, String md5) {
		char[] cs1 = new char[32];
		char[] cs2 = new char[16];
		for (int i = 0; i < 48; i += 3) {
			cs1[i / 3 * 2] = md5.charAt(i);
			cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
			cs2[i / 3] = md5.charAt(i + 1);
		}
		String salt = new String(cs2);
		return md5Hex(password + salt).equals(new String(cs1));
	}

	/**
	 * 获取十六进制字符串形式的MD5摘要
	 */
	public static String md5Hex(String src) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] bs = md5.digest(src.getBytes());
			return new String(new Hex().encode(bs));
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {
		// String password = generate("66", "37063766");
		System.out.println(generate("1234", "JYCS").equals(
				"91a093419e41512f714c0df33ad2457b"));
		System.out.println(generate("111111", "466706").equals(
				"29070cc5ca64a589656b961163975710"));
		System.out.println(generate("111111", "466706"));
		/*
		 * System.out.println(generate("1234", "02375218").equals(
		 * "eb7f7f4a09c5fe33217d953772af1619"));
		 */
		// String password1 = generate("222222", "123456");
		// Integer i01 = 59;
		// int i02 = 59;
		// Integer i03 = Integer.valueOf(59);
		// Integer i04 = new Integer(59);
		// System.out.println(i01 == i02);
		// System.out.println(i01 == i03);
		// System.out.println(i04 == i03);
		// System.out.println(i02 == i04);
		// System.out.println(password);
		// System.out.println(password1);
		// System.out.println(password.length()); //
		// System.out.println(verify("admin", password));
		// System.out.println("79faf82271944fe38c4f1d99be71bc9c".length());
	}

}

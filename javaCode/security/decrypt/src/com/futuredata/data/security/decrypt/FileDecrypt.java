/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.futuredata.data.security.decrypt;

import com.alibaba.fastjson.JSON;
import com.futuredata.data.security.bean.RsaBean;
import com.futuredata.data.security.bean.ValidateBean;
import com.futuredata.data.security.util.MD5Utils;
import com.futuredata.data.security.util.SecurityUtils;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.interfaces.RSAPrivateKey;

/**
 * 文件解密.
 *
 * @author kexin.ding
 * @create 2017-11-24 11:39
 **/
public class FileDecrypt {

	/**
	 * 解密.
	 */
	public static void decode(ValidateBean bean, String rsaFile, String srcFile, String targetFile) throws Exception {
		// 得到私钥
		RsaBean rsaBean = (RsaBean) readObject(rsaFile);
		RSAPrivateKey privateKey = rsaBean.getPriKey();

		byte[] resultBytes = rsaBean.getResultBytes();
		// 用私钥解密
		byte[] decBytes = RsaDecode.decrypt(privateKey, resultBytes);
		String decode = new String(decBytes, "utf-8");
		// System.out.println("RSA解密后是:" + decode);

		String key = SecurityUtils.getLocalMac();
		if (verify(decode, bean)) {
			key = decode;
		} else {
			throw new Exception("RSA文件验证失败");
		}
		AesDecode.decrypt(srcFile, targetFile, key);

	}

	/**
	 * 验证RSA文件合法性.
	 * @param decode 明文
	 * @param bean
	 * @return
	 */
	private static boolean verify(String decode, ValidateBean bean) {
		try {
			// 将json字符串转换成对象ALLOW_SINGLE_QUOTES ALLOW_UNQUOTED_CONTROL_CHARS
			ValidateBean srcBean = JSON.parseObject(decode, ValidateBean.class);

			boolean md5Flag = srcBean.getMd5().equals(MD5Utils.MD5(bean.getMacAddr() + bean.getPassword()));
			boolean foreverFlag = srcBean.getIsForever();
			if (foreverFlag) {
				return md5Flag;
			}
			// 是否有效期内
			long period = System.currentTimeMillis() - srcBean.getStart();
			if ((period < 0) || (period > srcBean.getExpired())) {
				System.out.println("RSA文件无效或过期");
				return false;
			}

			return md5Flag;

		} catch (Exception e) {
			System.out.println("RSA文件损坏");
		}

		System.out.println("RSA文件损坏");
		return false;
	}

	@SuppressWarnings("resource")
	private static Object readObject(String file) {
		FileInputStream reader;
		Object obj = null;
		try {
			reader = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(reader);
			obj = objectInputStream.readObject();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("读取文件失败");
		}

		return obj;
	}

	public static void main(String[] args) {
		//args = new String[]{"c:\\work\\123", "c:\\work\\id.rsa", "c:\\work\\12.exe", "123"};

		if (args == null || args.length != 4) {
			System.out.println("params error");
			System.out.println("param1： sourceFilePath 源文件，不许为空");
			System.out.println("param2： rsaFilePath 解密密钥文件，不许为空");
			System.out.println("param3： targetFilePath 解密后的文件，不许为空");
			System.out.println("param4： password 密码（六位数字字母组合）");
			return;
		}

		try {
			String sourceFilePath = args[0];
			String rsaFilePath = args[1];
			String targetFilePath = args[2];

			ValidateBean bean = new ValidateBean();
			if (args[3] != null) {
				bean.setPassword(args[3]);
			}
			bean.setMd5(MD5Utils.MD5(bean.getMacAddr() + bean.getPassword()));

			System.out.println("解密中...");
			decode(bean, rsaFilePath, sourceFilePath, targetFilePath);
			System.out.println("解密成功！");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("解密失败！");
		}

	}

}

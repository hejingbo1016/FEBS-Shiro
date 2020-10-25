package cc.mrbird.febs.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtil {

	public static final String MD5 = "MD5";

	public static byte[] encryptionStrBytes(String str, String algorithm) {
		byte[] bytes = null;
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(str.getBytes());
			bytes = md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null==bytes?null:bytes;
	}

	public static String bytesConvertToHexString(byte [] bytes) {
		StringBuilder sb = new StringBuilder();
		 for (byte aByte : bytes) {
			 String s= Integer.toHexString(0xff & aByte);
			 if(s.length()==1){
				 sb.append ( "0" ).append ( s );
			 }else{
				 sb.append(s);
			 }
		 }
		 return sb.toString();
	}

	/**
	 * 加密并返回32位字符串
	 * @param str
	 * @return
	 */
	public static String encryptionStr(String str, String algorithm) {
		byte[] bytes = encryptionStrBytes(str,algorithm);
		return bytesConvertToHexString(bytes);
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {

//		System.out.println(encryptionStr("123456789asdf","SHA-1"));

	}
}

package com.allroot.tool;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DESSecurity {
	
	 private static final String KEY_ALGORITHM = "DES";
	 
	 private static final String DEFAULT_CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding"; 
	 
	 private static final String DEFAULT_CHARACTER = "UTF-8";
	 
	public static String encrypt(String message, String key) throws Exception {  
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);  
  
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(DEFAULT_CHARACTER));  
  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);  
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);  
        IvParameterSpec iv = new IvParameterSpec(key.getBytes(DEFAULT_CHARACTER));  
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);  
  
        return toHexString(cipher.doFinal(message.getBytes(DEFAULT_CHARACTER)));  
    }  
  
  public static String decrypt(String message, String key) throws Exception {  
  
        byte[] bytesrc = convertHexString(message);  
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);  
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(DEFAULT_CHARACTER));  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);  
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);  
        IvParameterSpec iv = new IvParameterSpec(key.getBytes(DEFAULT_CHARACTER));  
  
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);  
  
        byte[] retByte = cipher.doFinal(bytesrc);  
        return new String(retByte);  
    } 
  public static byte[] convertHexString(String ss) {  
      byte digest[] = new byte[ss.length() / 2];  
      for (int i = 0; i < digest.length; i++) {  
          String byteString = ss.substring(2 * i, 2 * i + 2);  
          int byteValue = Integer.parseInt(byteString, 16);  
          digest[i] = (byte) byteValue;  
      }  

      return digest;  
  }  
  public static String toHexString(byte b[]) {  
      StringBuffer hexString = new StringBuffer();  
      for (int i = 0; i < b.length; i++) {  
          String plainText = Integer.toHexString(0xff & b[i]);  
          if (plainText.length() < 2)  
              plainText = "0" + plainText;  
          hexString.append(plainText);  
      }  

      return hexString.toString();  
  } 
	public static String getFixKey(String key) {
	     int length = key.length();
	     if( length < 8 ) {
	         for( int i=length;i<8; ++i )
	                key += 0;
	         return key;
	     } 
	    return key.substring(0, 8);
	}
  
  public static void main(String[] args) throws Exception {
	 
  } 
}

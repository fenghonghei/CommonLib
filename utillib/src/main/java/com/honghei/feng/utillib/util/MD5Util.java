package com.honghei.feng.utillib.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * author : feng
 * description ： md5工具类
 * creation time : 18-7-13下午2:59
 */
public class MD5Util {

  private static MessageDigest sMd5MessageDigest;
  public static StringBuilder sStringBuilder;

  static {
    try {
      sMd5MessageDigest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      // TODO Cyril: I'm quite sure about my "MD5" algorithm
      // but this is not a correct way to handle an exception ...
    }
    sStringBuilder = new StringBuilder();
  }

  private MD5Util() {
  }

  /**
   * Return a hash according to the MD5 algorithm of the given String.
   *
   * @param s The String whose hash is required
   * @return The MD5 hash of the given String
   */
  public static synchronized String md5(String s) {
    try {
      return md5(s.getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static synchronized String md5(File file) {
    try {
      FileInputStream in = new FileInputStream(file);
      ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
      byte[] buff = new byte[1024];
      int rc = 0;
      while ((rc = in.read(buff, 0, 1024)) > 0) {
        swapStream.write(buff, 0, rc);
      }
      return md5(swapStream.toByteArray());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static synchronized String md5(byte[] bytes) {
    sMd5MessageDigest.reset();
    sMd5MessageDigest.update(bytes);
    byte digest[] = sMd5MessageDigest.digest();

    sStringBuilder.setLength(0);
    for (byte d : digest) {
      final int b = d & 255;
      if (b < 16) {
        sStringBuilder.append('0');
      }
      sStringBuilder.append(Integer.toHexString(b));
    }
    return sStringBuilder.toString();
  }

  public static String byteArrayToHex(byte[] digest) {
    sStringBuilder.setLength(0);
    for (byte d : digest) {
      final int b = d & 255;
      if (b < 16) {
        sStringBuilder.append('0');
      }
      sStringBuilder.append(Integer.toHexString(b));
    }
    return sStringBuilder.toString();
  }

}

package com.honghei.feng.utillib.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * author : feng
 * description ： 转换工具类
 * creation time : 18-7-12下午6:47
 */
public class ConvertUtil {

  private ConvertUtil() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  private static final char hexDigits[] =
      {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

  /**
   * Bytes to bits.
   *
   * @param bytes The bytes.
   * @return bits
   */
  public static String bytes2Bits(final byte[] bytes) {
    if (bytes == null || bytes.length == 0) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (byte aByte : bytes) {
      for (int j = 7; j >= 0; --j) {
        sb.append(((aByte >> j) & 0x01) == 0 ? '0' : '1');
      }
    }
    return sb.toString();
  }

  /**
   * Bits to bytes.
   *
   * @param bits The bits.
   * @return bytes
   */
  public static byte[] bits2Bytes(String bits) {
    int lenMod = bits.length() % 8;
    int byteLen = bits.length() / 8;
    // add "0" until length to 8 times
    if (lenMod != 0) {
      for (int i = lenMod; i < 8; i++) {
        bits = "0" + bits;
      }
      byteLen++;
    }
    byte[] bytes = new byte[byteLen];
    for (int i = 0; i < byteLen; ++i) {
      for (int j = 0; j < 8; ++j) {
        bytes[i] <<= 1;
        bytes[i] |= bits.charAt(i * 8 + j) - '0';
      }
    }
    return bytes;
  }

  /**
   * Bytes to chars.
   *
   * @param bytes The bytes.
   * @return chars
   */
  public static char[] bytes2Chars(final byte[] bytes) {
    if (bytes == null) {
      return null;
    }
    int len = bytes.length;
    if (len <= 0) {
      return null;
    }
    char[] chars = new char[len];
    for (int i = 0; i < len; i++) {
      chars[i] = (char) (bytes[i] & 0xff);
    }
    return chars;
  }

  /**
   * Chars to bytes.
   *
   * @param chars The chars.
   * @return bytes
   */
  public static byte[] chars2Bytes(final char[] chars) {
    if (chars == null || chars.length <= 0) {
      return null;
    }
    int len = chars.length;
    byte[] bytes = new byte[len];
    for (int i = 0; i < len; i++) {
      bytes[i] = (byte) (chars[i]);
    }
    return bytes;
  }

  /**
   * Bytes to hex string.
   * <p>e.g. bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns "00A8"</p>
   *
   * @param bytes The bytes.
   * @return hex string
   */
  public static String bytes2HexString(final byte[] bytes) {
    if (bytes == null) {
      return "";
    }
    int len = bytes.length;
    if (len <= 0) {
      return "";
    }
    char[] ret = new char[len << 1];
    for (int i = 0, j = 0; i < len; i++) {
      ret[j++] = hexDigits[bytes[i] >> 4 & 0x0f];
      ret[j++] = hexDigits[bytes[i] & 0x0f];
    }
    return new String(ret);
  }

  /**
   * Hex string to bytes.
   * <p>e.g. hexString2Bytes("00A8") returns { 0, (byte) 0xA8 }</p>
   *
   * @param hexString The hex string.
   * @return the bytes
   */
  public static byte[] hexString2Bytes(String hexString) {
    if (isSpace(hexString)) {
      return null;
    }
    int len = hexString.length();
    if (len % 2 != 0) {
      hexString = "0" + hexString;
      len = len + 1;
    }
    char[] hexBytes = hexString.toUpperCase().toCharArray();
    byte[] ret = new byte[len >> 1];
    for (int i = 0; i < len; i += 2) {
      ret[i >> 1] = (byte) (hex2Int(hexBytes[i]) << 4 | hex2Int(hexBytes[i + 1]));
    }
    return ret;
  }

  private static int hex2Int(final char hexChar) {
    if (hexChar >= '0' && hexChar <= '9') {
      return hexChar - '0';
    } else if (hexChar >= 'A' && hexChar <= 'F') {
      return hexChar - 'A' + 10;
    } else {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Input stream to output stream.
   *
   * @param is The input stream.
   * @return output stream
   */
  public static ByteArrayOutputStream input2OutputStream(InputStream is) {
    if (is == null) {
      return null;
    }
    try {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      byte[] b = new byte[1024];
      int len;
      while ((len = is.read(b, 0, 1024)) != -1) {
        os.write(b, 0, len);
      }
      return os;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } finally {
      try {
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Output stream to input stream.
   *
   * @param out The output stream.
   * @return input stream
   */
  public ByteArrayInputStream output2InputStream(OutputStream out) {
    if (out == null) {
      return null;
    }
    return new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
  }

  /**
   * Input stream to bytes.
   *
   * @param is The input stream.
   * @return bytes
   */
  public static byte[] inputStream2Bytes(InputStream is) {
    if (is == null) {
      return null;
    }
    return input2OutputStream(is).toByteArray();
  }

  /**
   * Bytes to input stream.
   *
   * @param bytes The bytes.
   * @return input stream
   */
  public static InputStream bytes2InputStream(byte[] bytes) {
    if (bytes == null || bytes.length <= 0) {
      return null;
    }
    return new ByteArrayInputStream(bytes);
  }

  /**
   * Output stream to bytes.
   *
   * @param out The output stream.
   * @return bytes
   */
  public static byte[] outputStream2Bytes(OutputStream out) {
    if (out == null) {
      return null;
    }
    return ((ByteArrayOutputStream) out).toByteArray();
  }

  /**
   * Bytes to output stream.
   *
   * @param bytes The bytes.
   * @return output stream
   */
  public static OutputStream bytes2OutputStream(byte[] bytes) {
    if (bytes == null || bytes.length <= 0) {
      return null;
    }
    ByteArrayOutputStream os = null;
    try {
      os = new ByteArrayOutputStream();
      os.write(bytes);
      return os;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } finally {
      try {
        if (os != null) {
          os.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Input stream to string.
   *
   * @param is The input stream.
   * @param charsetName The name of charset.
   * @return string
   */
  public static String inputStream2String(InputStream is, String charsetName) {
    if (is == null || isSpace(charsetName)) {
      return "";
    }
    try {
      return new String(inputStream2Bytes(is), charsetName);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * String to input stream.
   *
   * @param string The string.
   * @param charsetName The name of charset.
   * @return input stream
   */
  public static InputStream string2InputStream(String string, String charsetName) {
    if (string == null || isSpace(charsetName)) {
      return null;
    }
    try {
      return new ByteArrayInputStream(string.getBytes(charsetName));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Output stream to string.
   *
   * @param out The output stream.
   * @param charsetName The name of charset.
   * @return string
   */
  public static String outputStream2String(OutputStream out, String charsetName) {
    if (out == null || isSpace(charsetName)) {
      return "";
    }
    try {
      return new String(outputStream2Bytes(out), charsetName);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * String to output stream.
   *
   * @param string The string.
   * @param charsetName The name of charset.
   * @return output stream
   */
  public static OutputStream string2OutputStream(String string, String charsetName) {
    if (string == null || isSpace(charsetName)) {
      return null;
    }
    try {
      return bytes2OutputStream(string.getBytes(charsetName));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Bitmap to bytes.
   *
   * @param bitmap The bitmap.
   * @param format The format of bitmap.
   * @return bytes
   */
  public static byte[] bitmap2Bytes(Bitmap bitmap, Bitmap.CompressFormat format) {
    if (bitmap == null) {
      return null;
    }
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(format, 100, baos);
    return baos.toByteArray();
  }

  /**
   * Bytes to bitmap.
   *
   * @param bytes The bytes.
   * @return bitmap
   */
  public static Bitmap bytes2Bitmap(byte[] bytes) {
    return (bytes == null || bytes.length == 0)
        ? null
        : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
  }

  /**
   * Drawable to bitmap.
   *
   * @param drawable The drawable.
   * @return bitmap
   */
  public static Bitmap drawable2Bitmap(Drawable drawable) {
    if (drawable instanceof BitmapDrawable) {
      BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
      if (bitmapDrawable.getBitmap() != null) {
        return bitmapDrawable.getBitmap();
      }
    }
    Bitmap bitmap;
    if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
      bitmap = Bitmap.createBitmap(1, 1,
          drawable.getOpacity() != PixelFormat.OPAQUE
              ? Bitmap.Config.ARGB_8888
              : Bitmap.Config.RGB_565);
    } else {
      bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
          drawable.getIntrinsicHeight(),
          drawable.getOpacity() != PixelFormat.OPAQUE
              ? Bitmap.Config.ARGB_8888
              : Bitmap.Config.RGB_565);
    }
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    drawable.draw(canvas);
    return bitmap;
  }

  /**
   * Bitmap to drawable.
   *
   * @param bitmap The bitmap.
   * @return drawable
   */
  public static Drawable bitmap2Drawable(Bitmap bitmap) {
    return bitmap == null ? null : new BitmapDrawable(AppUtil.getApp().getResources(), bitmap);
  }

  /**
   * Drawable to bytes.
   *
   * @param drawable The drawable.
   * @param format The format of bitmap.
   * @return bytes
   */
  public static byte[] drawable2Bytes(Drawable drawable,
      Bitmap.CompressFormat format) {
    return drawable == null ? null : bitmap2Bytes(drawable2Bitmap(drawable), format);
  }

  /**
   * Bytes to drawable.
   *
   * @param bytes The bytes.
   * @return drawable
   */
  public static Drawable bytes2Drawable(byte[] bytes) {
    return bytes == null ? null : bitmap2Drawable(bytes2Bitmap(bytes));
  }

  /**
   * View to bitmap.
   *
   * @param view The view.
   * @return bitmap
   */
  public static Bitmap view2Bitmap(View view) {
    if (view == null) {
      return null;
    }
    Bitmap ret =
        Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(ret);
    Drawable bgDrawable = view.getBackground();
    if (bgDrawable != null) {
      bgDrawable.draw(canvas);
    } else {
      canvas.drawColor(Color.WHITE);
    }
    view.draw(canvas);
    return ret;
  }

  /**
   * Value of dp to value of px.
   *
   * @param dpValue The value of dp.
   * @return value of px
   */
  public static int dp2px(float dpValue) {
    float scale = AppUtil.getApp().getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

  /**
   * Value of px to value of dp.
   *
   * @param pxValue The value of px.
   * @return value of dp
   */
  public static int px2dp(float pxValue) {
    float scale = AppUtil.getApp().getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }

  /**
   * Value of sp to value of px.
   *
   * @param spValue The value of sp.
   * @return value of px
   */
  public static int sp2px(float spValue) {
    float fontScale = AppUtil.getApp().getResources().getDisplayMetrics().scaledDensity;
    return (int) (spValue * fontScale + 0.5f);
  }

  /**
   * Value of px to value of sp.
   *
   * @param pxValue The value of px.
   * @return value of sp
   */
  public static int px2sp(float pxValue) {
    float fontScale = AppUtil.getApp().getResources().getDisplayMetrics().scaledDensity;
    return (int) (pxValue / fontScale + 0.5f);
  }

  private static boolean isSpace(String s) {
    if (s == null) {
      return true;
    }
    for (int i = 0, len = s.length(); i < len; ++i) {
      if (!Character.isWhitespace(s.charAt(i))) {
        return false;
      }
    }
    return true;
  }
}

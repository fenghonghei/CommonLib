package com.honghei.feng.utillib.util;

import static android.Manifest.permission.READ_PHONE_STATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * author : feng
 * description ： 设备工具类
 * creation time : 18-7-12下午6:58
 */
public class DeviceUtil {

  private DeviceUtil() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }


  /**
   * Return the version name of device's system.
   *
   * @return the version name of device's system
   */
  public static String getSDKVersionName() {
    return android.os.Build.VERSION.RELEASE;
  }

  /**
   * Return version code of device's system.
   *
   * @return version code of device's system
   */
  public static int getSDKVersionCode() {
    return android.os.Build.VERSION.SDK_INT;
  }

  /**
   * Return the unique device id.
   * <p>Must hold
   * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
   *
   * @return the unique device id
   */
  @SuppressLint("HardwareIds")
  @RequiresPermission(READ_PHONE_STATE)
  public static String getDeviceId() {
    TelephonyManager tm =
        (TelephonyManager) AppUtil.getApp().getSystemService(Context.TELEPHONY_SERVICE);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      if (tm == null) {
        return "";
      }
      String imei = tm.getImei();
      if (!TextUtils.isEmpty(imei)) {
        return imei;
      }
      String meid = tm.getMeid();
      return TextUtils.isEmpty(meid) ? "" : meid;

    }
    return tm != null ? tm.getDeviceId() : "";
  }

  /**
   * Return the android id of device.
   *
   * @return the android id of device
   */
  @SuppressLint("HardwareIds")
  public static String getAndroidID() {
    String id = Settings.Secure.getString(
        AppUtil.getApp().getContentResolver(),
        Settings.Secure.ANDROID_ID
    );
    return id == null ? "" : id;
  }

  /**
   * Return the serial of device.
   *
   * @return the serial of device
   */
  @SuppressLint("HardwareIds")
  @RequiresPermission(READ_PHONE_STATE)
  public static String getSerial() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      return Build.getSerial();
    } else {
      return Build.SERIAL;
    }
  }

  /**
   * Return the IMEI.
   * <p>Must hold
   * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
   *
   * @return the IMEI
   */
  @SuppressLint("HardwareIds")
  @RequiresPermission(READ_PHONE_STATE)
  public static String getIMEI() {
    TelephonyManager tm = (TelephonyManager) AppUtil.getApp().getSystemService(Context.TELEPHONY_SERVICE);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      return tm != null ? tm.getImei() : "";
    }
    return tm != null ? tm.getDeviceId() : "";
  }

  /**
   * Return the MEID.
   * <p>Must hold
   * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
   *
   * @return the MEID
   */
  @SuppressLint("HardwareIds")
  @RequiresPermission(READ_PHONE_STATE)
  public static String getMEID() {
    TelephonyManager tm =
        (TelephonyManager) AppUtil.getApp().getSystemService(Context.TELEPHONY_SERVICE);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      return tm != null ? tm.getMeid() : "";
    } else {
      return tm != null ? tm.getDeviceId() : "";
    }
  }

  /**
   * Return the IMSI.
   * <p>Must hold
   * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
   *
   * @return the IMSI
   */
  @SuppressLint("HardwareIds")
  @RequiresPermission(READ_PHONE_STATE)
  public static String getIMSI() {
    TelephonyManager tm = (TelephonyManager) AppUtil.getApp().getSystemService(Context.TELEPHONY_SERVICE);
    return tm != null ? tm.getSubscriberId() : "";
  }

  /**
   * Returns the current phone type.
   *
   * @return the current phone type
   * <ul>
   * <li>{@link TelephonyManager#PHONE_TYPE_NONE}</li>
   * <li>{@link TelephonyManager#PHONE_TYPE_GSM }</li>
   * <li>{@link TelephonyManager#PHONE_TYPE_CDMA}</li>
   * <li>{@link TelephonyManager#PHONE_TYPE_SIP }</li>
   * </ul>
   */
  public static int getPhoneType() {
    TelephonyManager tm = (TelephonyManager) AppUtil.getApp().getSystemService(Context.TELEPHONY_SERVICE);
    return tm != null ? tm.getPhoneType() : -1;
  }

  @SuppressLint({"HardwareIds", "MissingPermission"})
  private static String getMacAddressByWifiInfo() {
    try {
      Context context = AppUtil.getApp().getApplicationContext();
      WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
      if (wifi != null) {
        WifiInfo info = wifi.getConnectionInfo();
        if (info != null) {
          return info.getMacAddress();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "02:00:00:00:00:00";
  }

  private static String getMacAddressByNetworkInterface() {
    try {
      Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
      while (nis.hasMoreElements()) {
        NetworkInterface ni = nis.nextElement();
        if (ni == null || !ni.getName().equalsIgnoreCase("wlan0")) {
          continue;
        }
        byte[] macBytes = ni.getHardwareAddress();
        if (macBytes != null && macBytes.length > 0) {
          StringBuilder sb = new StringBuilder();
          for (byte b : macBytes) {
            sb.append(String.format("%02x:", b));
          }
          return sb.substring(0, sb.length() - 1);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "02:00:00:00:00:00";
  }

  private static String getMacAddressByInetAddress() {
    try {
      InetAddress inetAddress = getInetAddress();
      if (inetAddress != null) {
        NetworkInterface ni = NetworkInterface.getByInetAddress(inetAddress);
        if (ni != null) {
          byte[] macBytes = ni.getHardwareAddress();
          if (macBytes != null && macBytes.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (byte b : macBytes) {
              sb.append(String.format("%02x:", b));
            }
            return sb.substring(0, sb.length() - 1);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "02:00:00:00:00:00";
  }

  private static InetAddress getInetAddress() {
    try {
      Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
      while (nis.hasMoreElements()) {
        NetworkInterface ni = nis.nextElement();
        // To prevent phone of xiaomi return "10.0.2.15"
        if (!ni.isUp()) {
          continue;
        }
        Enumeration<InetAddress> addresses = ni.getInetAddresses();
        while (addresses.hasMoreElements()) {
          InetAddress inetAddress = addresses.nextElement();
          if (!inetAddress.isLoopbackAddress()) {
            String hostAddress = inetAddress.getHostAddress();
            if (hostAddress.indexOf(':') < 0) {
              return inetAddress;
            }
          }
        }
      }
    } catch (SocketException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Return the manufacturer of the product/hardware.
   * <p>e.g. Xiaomi</p>
   *
   * @return the manufacturer of the product/hardware
   */
  public static String getManufacturer() {
    return Build.MANUFACTURER;
  }

  /**
   * Return the model of device.
   * <p>e.g. MI2SC</p>
   *
   * @return the model of device
   */
  public static String getModel() {
    String model = Build.MODEL;
    if (model != null) {
      model = model.trim().replaceAll("\\s*", "");
    } else {
      model = "";
    }
    return model;
  }

  /**
   * Return whether the device is phone.
   *
   * @return {@code true}: yes<br>{@code false}: no
   */
  public static boolean isPhone() {
    TelephonyManager tm = (TelephonyManager) AppUtil.getApp().getSystemService(Context.TELEPHONY_SERVICE);
    return tm != null && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
  }
}

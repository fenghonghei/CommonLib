package com.honghei.feng.cachelib;

import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

    public static File getCachedFile(Context context, String url) {
        return new File(context.getCacheDir(), MD5Util.md5(url));
    }

    public static String FileToString(File file, String encoding) throws IOException {
        InputStream is = new FileInputStream(file);
        return inputStreamToString(is, encoding);
    }

    public static void StringToFile(String string, File file, String encoding) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(string.getBytes(encoding));
        inputStreamToFile(stream, file);
        stream.close();
    }

    private static String inputStreamToString(InputStream in, String encoding) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[8 * 1024];
        int count;
        while ((count = in.read(data, 0, data.length)) != -1) {
            outStream.write(data, 0, count);
        }
        String s = new String(outStream.toByteArray(), encoding);
        outStream.close();
        return s;
    }

    private static void inputStreamToFile(InputStream is, File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        inputStreamToOutputStream(is, fileOutputStream);
        fileOutputStream.close();
    }

    private static void inputStreamToOutputStream(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[8 * 1024];
        int count;
        while ((count = is.read(buffer)) > 0) {
            os.write(buffer, 0, count);
        }
    }

    public static boolean isFileExpired(File file, long expiredTime) {
        long current = System.currentTimeMillis();
        long lastModify = file.lastModified();
        return current - lastModify > expiredTime;
    }


}

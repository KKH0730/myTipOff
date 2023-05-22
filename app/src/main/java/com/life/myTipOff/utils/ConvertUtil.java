package com.life.myTipOff.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ConvertUtil {

    @SuppressLint("Recycle")
    public static File convertUriToFile(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                ParcelFileDescriptor fileDescriptor = contentResolver.openFile(uri, "r", null);
                FileInputStream fileInputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
                return convertInputStreamToFile(fileInputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if ("content".equals(uri.getScheme())) {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver().query(uri, projection, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        String filePath = cursor.getString(columnIndex);
                        if (filePath != null) {
                            return new File(filePath);
                        }
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            } else if ("file".equals(uri.getScheme())) {
                String filePath = uri.getPath();
                if (filePath != null) {
                    return new File(filePath);
                }
            }
        }
        return null;
    }

    private static File convertInputStreamToFile(InputStream inputStream) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile(String.valueOf(inputStream.hashCode()), ".jpg");
            tempFile.deleteOnExit();
            copyInputStreamToFile(inputStream, tempFile);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    private static void copyInputStreamToFile(InputStream inputStream, File file) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int read;
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static float getFileSize(File file) {
        long fileSizeInBytes = file.length();
        float fileSizeInKB = fileSizeInBytes / 1024L;
        float fileSizeInMB = fileSizeInKB / 1024f;

        System.out.println("파일 크기: " + fileSizeInBytes + "," + fileSizeInKB + "," + fileSizeInMB);
        return fileSizeInMB;
    }
}
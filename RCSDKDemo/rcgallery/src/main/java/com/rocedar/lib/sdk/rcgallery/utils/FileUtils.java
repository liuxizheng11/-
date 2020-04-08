package com.rocedar.lib.sdk.rcgallery.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * FileUtils
 * Created by Yancy on 2015/12/2.
 */
public class FileUtils {


    private final static String PATTERN = "yyyyMMddHHmmss";    // 时间戳命名


    /**
     * 创建文件
     *
     * @param context  context
     * @param filePath 文件路径
     * @return file
     */
    public static File createTmpFile(Context context, String filePath) {

        String timeStamp = new SimpleDateFormat(PATTERN, Locale.CHINA).format(new Date());

        String externalStorageState = Environment.getExternalStorageState();

        File dir = new File(Environment.getExternalStorageDirectory() + filePath);

        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return new File(dir, timeStamp + ".jpg");
        } else {
            File cacheDir = context.getCacheDir();
            return new File(cacheDir, timeStamp + ".jpg");
        }

    }


    /**
     * 创建初始文件夹。保存拍摄图片和裁剪后的图片
     *
     * @param filePath 文件夹路径
     */
    public static void createFile(String filePath) {
        String externalStorageState = Environment.getExternalStorageState();

        File dir = new File(Environment.getExternalStorageDirectory() + filePath);
        File cropFile = new File(Environment.getExternalStorageDirectory() + filePath + "/crop");

        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
            if (!cropFile.exists()) {
                cropFile.mkdirs();
            }

            File file = new File(cropFile, ".nomedia");    // 创建忽视文件。   有该文件，系统将检索不到此文件夹下的图片。
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public static String getFilePath(Context context) {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getPath();
        } else {
            return context.getCacheDir().getAbsolutePath();
        }
    }


    /**
     * @param filePath 文件夹路径
     * @return 截图完成的 file
     */
    public static File getCorpFile(String filePath) {
        String timeStamp = new SimpleDateFormat(PATTERN, Locale.CHINA).format(new Date());
        return new File(Environment.getExternalStorageDirectory() + filePath + "/crop/" + timeStamp + ".jpg");
    }


    /**
     * 保存图片
     *
     * @param context
     * @param fromFile
     * @param toFile
     * @return
     */
    public static File copyFile(Context context, File fromFile, File toFile) {
        if (!fromFile.exists()) {
            return null;
        }
        if (!fromFile.isFile()) {
            return null;
        }
        if (!fromFile.canRead()) {
            return null;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        try {
            java.io.FileInputStream fosfrom = new java.io.FileInputStream(
                    fromFile);
            java.io.FileOutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c); // 将内容写到新文件当中
            }
            fosfrom.close();
            fosto.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return toFile;
    }

    public static boolean saveBitmap(String fileName, Bitmap bitmaps, String hz) {
        if (bitmaps == null) {
            return false;
        }
        try {
            ByteArrayOutputStream baos = compressImage(bitmaps, hz);
            File file = new File(fileName);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            baos.writeTo(fos);
            if (!bitmaps.isRecycled()) {
                bitmaps.recycle();
            }
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static ByteArrayOutputStream compressImage(Bitmap bitmap, String hz) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;
        if (hz.contains("jpg") || hz.contains("JPG") || hz.contains("jpeg")
                || hz.contains("JPEG")) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        } else if (hz.contains("PNG") || hz.contains("png")) {
            bitmap.compress(Bitmap.CompressFormat.PNG, options, baos);
        } else if (hz.contains("WEBP") || hz.contains("webp")) {
            bitmap.compress(Bitmap.CompressFormat.WEBP, options, baos);
        } else {
            bitmap.compress(Bitmap.CompressFormat.PNG, options, baos);
        }
        while (baos.toByteArray().length / 1024 > 200 && options > 30) { //
            // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            if (hz.contains("jpg") || hz.contains("JPG") || hz.contains("jpeg")
                    || hz.contains("JPEG")) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            } else if (hz.contains("PNG") || hz.contains("png")) {
                bitmap.compress(Bitmap.CompressFormat.PNG, options, baos);
            } else if (hz.contains("WEBP") || hz.contains("webp")) {
                bitmap.compress(Bitmap.CompressFormat.WEBP, options, baos);
            } else {
                bitmap.compress(Bitmap.CompressFormat.PNG, options, baos);
            }
        }
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return baos;//
    }




}
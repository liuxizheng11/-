package com.rocedar.sdk.iting.device.function;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

import com.rocedar.lib.base.unit.RCJavaUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2019/4/30 12:18 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class PhotoImageUtil {

    public static String filePath = Environment.getExternalStorageDirectory() + "/rocedar/photo/";


    public static String getImage(String oldFilePath, int wight) {
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String tempFileName = filePath + new Date().getTime() + ".bmp";
        saveBitmap(tempFileName, compressBySize(oldFilePath, wight, wight));
        return tempFileName;
    }

    /**
     * 传入图片url,通过压缩图片的尺寸来压缩图片大小
     *
     * @param pathName     图片的完整路径
     * @param targetWidth  缩放的目标宽度
     * @param targetHeight 缩放的目标高度
     * @return 缩放后的图片
     */
    public static Bitmap compressBySize(String pathName, int targetWidth, int targetHeight) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = targetHeight;// 这里设置高度为800f
        float ww = targetWidth;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w >= h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(pathName, newOpts);
        return zoomBitmap(bitmap, targetWidth, targetHeight);// 压缩好比例大小后再进行质量压缩
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, float vw, float vh) {
        float width = bitmap.getWidth();//获得图片宽高
        float height = bitmap.getHeight();

        float scaleWidht, scaleHeight, x, y;//图片缩放倍数以及x，y轴平移位置
        Bitmap newbmp = null; //新的图片
        Matrix matrix = new Matrix();//变换矩阵
        if ((width / height) <= vw / vh) {//当宽高比大于所需要尺寸的宽高比时以宽的倍数为缩放倍数
            scaleWidht = vw / width;
            scaleHeight = scaleWidht;
            y = ((height * scaleHeight - vh) / 2) / scaleHeight;// 获取bitmap源文件中y做表需要偏移的像数大小
            x = 0;
        } else {
            scaleWidht = vh / height;
            scaleHeight = scaleWidht;
            x = ((width * scaleWidht - vw) / 2) / scaleWidht;// 获取bitmap源文件中x做表需要偏移的像数大小
            y = 0;
        }
        matrix.postScale(scaleWidht / 1f, scaleHeight / 1f);
        try {
            //获得新的图片 （原图，x轴起始位置，y轴起始位置，x轴结束位置，Y轴结束位置，缩放矩阵，是否过滤原图）为防止报错取绝对值
            if (width - x > 0 && height - y > 0 && bitmap != null)
                newbmp = Bitmap.createBitmap(bitmap, (int) Math.abs(x), (int) Math.abs(y), (int) Math.abs(width - x),
                        (int) Math.abs(height - y), matrix, false);
            // createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()
        } catch (Exception e) {//如果报错则返回原图，不至于为空白
            e.printStackTrace();
            return bitmap;
        }
        return newbmp;
    }

    public static boolean saveBitmap(String newFile, Bitmap bitmaps) {
        if (bitmaps == null) {
            return false;
        }
        try {
            ByteArrayOutputStream baos = compressImage(bitmaps, RCJavaUtil.getExtensionNames(newFile));
            File file = new File(newFile);
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
        while (baos.toByteArray().length / 1024 > 80 && options > 10) { //
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

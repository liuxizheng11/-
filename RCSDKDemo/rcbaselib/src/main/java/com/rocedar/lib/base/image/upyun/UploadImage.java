package com.rocedar.lib.base.image.upyun;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;

import com.rocedar.lib.base.manage.RCSDKManage;
import com.rocedar.lib.base.unit.RCDateUtil;
import com.rocedar.lib.base.unit.RCJavaUtil;
import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.lib.base.unit.RCUtilEncode;
import com.rocedar.lib.base.userinfo.RCSPUserInfo;
import com.rocedar.lib.sdk.rcgallery.utils.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class UploadImage {

    public static class Configuration {
        /**
         * 上传到服务器的图片短变最大尺寸
         */
        public static int maxLength = 720;
    }

    public interface Models {
        public final int UserCircle = 0;
        public final int UserHead = 1;
    }

    public interface UploadListener {
        public void onProgressListener(int p);

        public void onUpLoadOverOk(String url, int imageW, int imageH);

        public void onUpLoadOverError();
    }

    private UploadListener uploadListener;

    // 空间名
    private String SPACE = "dongya";
    // 表单密钥
    private String KEY = "tlB34gbnY59j8Y7nhJPz/YXti0w=";
    // 本地文件路径
    private String localFilePath;
    // 保存到又拍云的路径
    private String savePath = "";
    // 文件后缀
    private String imageType = "";

    private static final String TAG = "UPYUN";

    /*
     * u(user)/[user_id]/h(head)/xxx.png
     *
     * u(user)/[user_id]/v(vrify)/xxx.png
     */


    public UploadImage(UploadListener listener, File file, int type) {
//        if (f < 2) {//密度
//            Configuration.maxLength = 640;
//        }
        String info = RCSPUserInfo.getLastUserId() + "";
        if (!info.equals("") && info.length() > 4) {
            info = RCUtilEncode.getMD5StrLower32(info).substring(0, 4);
        }
        uploadListener = listener;
        localFilePath = file.getPath();
        imageType = RCJavaUtil.getExtensionNames(file.getName());
        if (type == Models.UserHead) {
            savePath = "/" + RCSDKManage.getInstance().getAPPTAG() +
                    "/u/" + (info.equals("") ? "" : "/" + info)
                    + "/h/"
//                    + DYJavaUtil.getNowDate("yyyyMMddHHmmss")
                    + RCUtilEncode.getMd5StrLower16(localFilePath)
                    + "." + imageType;
        } else if (type == Models.UserCircle) {
            savePath = "/" + RCSDKManage.getInstance().getAPPTAG() +
                    "/u/" + (info.equals("") ? "" : "/" + info) + "/m/"
                    + RCDateUtil.getFormatNow("yyyyMM") + "/"
                    + RCUtilEncode.getMd5StrLower16(localFilePath)
                    + "." + imageType;
        }
        new UploadTask().execute();
    }

    private int GcCount = 0;

    public String transImage(String fromFile, int beSize) {
        // ThumbnailUtils.extractThumbnail(source, , height)
        Bitmap bitmap = null;
        try {
            if (getExtensionNames(fromFile).equals("gif")) {
                RCLog.e("上传的是GIF");
                return fromFile;
            }
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
            newOpts.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeFile(fromFile, newOpts);// 此时返回bm为空
            newOpts.inJustDecodeBounds = false;
            int bitmapWidth = newOpts.outWidth;
            int bitmapHeight = newOpts.outHeight;
            // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            boolean has = false;
            int be = 1;// be=1表示不缩放
            int rew = 0;
            int reh = 0;
            if (bitmapWidth >= bitmapHeight) {
                if (bitmapHeight > Configuration.maxLength) {
                    reh = Configuration.maxLength;
                    rew = bitmapWidth * Configuration.maxLength / bitmapHeight;
                    be = (int) bitmapHeight / Configuration.maxLength;
                    if (be >= 1 && bitmapHeight % Configuration.maxLength != 0) {
                        has = true;
                    }
                }
            } else {

                if (bitmapWidth > Configuration.maxLength) {
                    rew = Configuration.maxLength;
                    reh = bitmapHeight * Configuration.maxLength / bitmapWidth;
                    be = (int) bitmapWidth / Configuration.maxLength;
                    if (be >= 1 && bitmapHeight % Configuration.maxLength != 0) {
                        has = true;
                    }
                }
            }
            if (be <= 0)
                be = 1;
            if (beSize > 1) {
                be = beSize;
            }
            newOpts.inSampleSize = be;// 设置缩放比例
            if (be == 1) {
                bitmap = BitmapFactory.decodeFile(fromFile);
            } else {
                bitmap = BitmapFactory.decodeFile(fromFile, newOpts);
            }
            // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
            if (has) {
                bitmap = transImageResize(bitmap, rew, reh);
            }
            // save file
            String temp = FileUtils.createTmpFile(RCSDKManage.getInstance().getContext()
                    , "/rocedar/t/").getAbsolutePath();
            if (!FileUtils.saveBitmap(temp, bitmap, imageType)) {
                return null;
            }
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            return temp;
        } catch (OutOfMemoryError e) {
            GcCount++;
            if (null != bitmap && !bitmap.isRecycled())
                bitmap.recycle();
            RCLog.d("gc-ChoosePhotoListActivity");
            System.gc();
            if (GcCount < 5) {
                return transImage(fromFile, GcCount);
            } else {
                return "";
            }
        }
    }

    // 使用Bitmap加Matrix来缩放
    public Bitmap transImageResize(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    private int count = 0;

    public class UploadTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            count++;
            String filename = transImage(localFilePath, 0);
            if (filename == null) {
                uploadListener.onUpLoadOverError();
                return "";
            }
            final File localFile = new File(filename);
            if (!localFile.isFile()) {
                uploadListener.onUpLoadOverError();
                return "";
            }
            try {
                UpyunUnit.UploadFile(localFile, savePath, new UpYunUploadListener() {
                    @Override
                    public void upLoadError(String msg) {

                    }

                    @Override
                    public void onComplete(boolean isSuccess, String result) {
                        try {
                            if (isSuccess) {
                                if (!result.equals("")
                                        && new JSONObject(result).has("url")) {
                                    if (localFile.isFile()) {
                                        localFile.delete();
                                    }
                                    JSONObject jsonObject = new JSONObject(
                                            result);
                                    uploadListener
                                            .onUpLoadOverOk(jsonObject.optString("url"),
                                                    jsonObject.optInt("image-width"), jsonObject.optInt("image-height"));
                                } else {
                                    if (count < 3) {
                                        new UploadTask().execute();
                                    } else {
                                        if (localFile.isFile()) {
                                            localFile.delete();
                                        }
                                        uploadListener.onUpLoadOverError();
                                    }
                                }
                            } else {
                                if (count < 3) {
                                    new UploadTask().execute();
                                } else {
                                    if (localFile.isFile()) {
                                        localFile.delete();
                                    }
                                    uploadListener.onUpLoadOverError();
                                }
                            }
                        } catch (JSONException e) {
                            if (localFile.isFile()) {
                                localFile.delete();
                            }
                            uploadListener.onUpLoadOverError();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void upLoadIn(long bytesWrite, long contentLength) {
                        RCLog.e(TAG, (100 * bytesWrite) / contentLength + "%");
                        uploadListener.onProgressListener((int) ((100 * bytesWrite) / contentLength));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "result";
        }

    }

    /**
     * 取文件后缀
     *
     * @param filename 文件名称
     * @return
     */
    public static String getExtensionNames(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }
}
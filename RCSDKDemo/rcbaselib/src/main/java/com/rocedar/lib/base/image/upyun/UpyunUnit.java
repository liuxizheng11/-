package com.rocedar.lib.base.image.upyun;


import com.upyun.library.common.Params;
import com.upyun.library.common.UploadEngine;
import com.upyun.library.listener.UpCompleteListener;
import com.upyun.library.listener.UpProgressListener;
import com.upyun.library.utils.UpYunUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/4/26 下午3:35
 * 版本：V1.0.01
 * 描述：upyun上传工具类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class UpyunUnit {

    // 空间名
    private final static String SPACE_DY = "dongya";

    //操作员
    private static String OPERATER_DY = "doit";
    //密码
    private static String PASSWORD_DY = "do2016it";


    public static void UploadFile(File file, String savePath, final UpYunUploadListener upYunUploadListener) {
        if (file == null) {
            upYunUploadListener.upLoadError("找不到文件");
            return;
        }
        UpProgressListener progressListener = new UpProgressListener() {
            @Override
            public void onRequestProgress(final long bytesWrite, final long contentLength) {
//                uploadProgress.setProgress((int) ((100 * bytesWrite) / contentLength));
//                textView.setText((100 * bytesWrite) / contentLength + "%");
//                Log.e(TAG, (100 * bytesWrite) / contentLength + "%");
                upYunUploadListener.upLoadIn(bytesWrite, contentLength);
            }
        };

        //结束回调，不可为空
        UpCompleteListener completeListener = new UpCompleteListener() {
            @Override
            public void onComplete(boolean isSuccess, String result) {
//                textView.setText(isSuccess + ":" + result);
//                Log.e(TAG, isSuccess + ":" + result);
                upYunUploadListener.onComplete(isSuccess, result);
            }
        };
        final Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(Params.SAVE_KEY, savePath);
        paramsMap.put(Params.CONTENT_MD5, UpYunUtils.md5Hex(file));
        paramsMap.put(Params.BUCKET, SPACE_DY);
        UploadEngine.getInstance().formUpload(file, paramsMap,
                OPERATER_DY, UpYunUtils.md5(PASSWORD_DY), completeListener, progressListener);

    }


//    //断点续传使用 DEMO
//    public void resumeUpdate(final File file, final Map<String, String> params) {
//
//        final ResumeUploader uploader = new ResumeUploader(SPACE, OPERATER, UpYunUtils.md5(PASSWORD));
//
//        //设置 MD5 校验
//        uploader.setCheckMD5(true);
//
//        //设置进度监听
//        uploader.setOnProgressListener(new ResumeUploader.OnProgressListener() {
//            @Override
//            public void onProgress(int index, int total) {
//            }
//        });
//
//        new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    uploader.upload(file, "/test1.txt", params);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (UpYunException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.run();
//    }


}

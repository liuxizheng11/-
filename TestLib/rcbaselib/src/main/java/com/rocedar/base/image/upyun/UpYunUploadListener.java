package com.rocedar.base.image.upyun;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/4/26 下午4:05
 * 版本：V1.0.01
 * 描述：upyun上传监听
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public interface UpYunUploadListener {

    void upLoadError(String msg);

    void onComplete(boolean isSuccess, String result);

    void upLoadIn(long bytesWrite, long contentLength);

}

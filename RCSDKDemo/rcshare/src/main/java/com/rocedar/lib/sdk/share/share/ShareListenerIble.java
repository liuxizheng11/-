package com.rocedar.lib.sdk.share.share;

/**
 * Created by phj on 2016/12/5.
 * <p>
 * 分享监听接口
 */
public interface ShareListenerIble {

    /* 成功*/
    void onSuccess();

    /* 出错*/
    void onError();

    /* 取消*/
    void onCancel();

}

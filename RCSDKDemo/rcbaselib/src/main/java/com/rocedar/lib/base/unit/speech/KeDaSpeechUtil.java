package com.rocedar.lib.base.unit.speech;

import android.app.Activity;

import com.rocedar.lib.sdk.kedaspeech.RCKeDaSpeechUtil;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/11/14 9:44 AM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class KeDaSpeechUtil implements IRCSpeech {

    private RCKeDaSpeechUtil speechUtil;

    private Activity activity;

    public KeDaSpeechUtil(Activity activity) {
        this.activity = activity;
        speechUtil = new RCKeDaSpeechUtil(activity);
    }


    @Override
    public void start() {
        speechUtil.onStart();
    }

    @Override
    public void stop() {
        speechUtil.onStop();
    }

    @Override
    public void cancel() {
        speechUtil.onCancel();
    }

    @Override
    public void onDestroy() {
        speechUtil.onDestroy();
    }

    @Override
    public void setListener(final IRCSpeechListener listener) {
        speechUtil.setListener(new com.rocedar.lib.sdk.kedaspeech.IRCSpeechListener() {
            @Override
            public void onStart() {
                listener.onStart();
            }

            @Override
            public void onStop() {
                listener.onStop();
            }

            @Override
            public void onRmsChanged(int rmsdB) {
                listener.onRmsChanged(rmsdB);
            }

            @Override
            public void onError() {
                listener.onError();

            }

            @Override
            public void results(String info) {
                listener.results(info);

            }

            @Override
            public void partialResults(String info) {
                listener.partialResults(info);

            }
        });
    }
}

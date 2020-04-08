package com.rocedar.lib.sdk.kedaspeech;

/**
 * 项目名称：瑰柏SDK-
 * <p>
 * 作者：phj
 * 日期：2018/11/6 4:58 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface IRCSpeechListener {

    void onStart();

    void onStop();

    void onRmsChanged(int rmsdB);

    void onError();

    void results(String info);

    void partialResults(String info);

}

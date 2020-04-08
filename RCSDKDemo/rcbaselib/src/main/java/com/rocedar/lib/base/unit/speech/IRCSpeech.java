package com.rocedar.lib.base.unit.speech;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/11/7 5:00 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface IRCSpeech {

    void start();

    void stop();

    void cancel();

    void onDestroy();

    void setListener(IRCSpeechListener listener);
}

package com.rocedar.sdk.iting.device.listener;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2019/4/30 9:33 AM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCITingPhotoListener {

    void start();

    void progress(int current);

    void over();

    void maxValue(int max);

    void error(String info);


}


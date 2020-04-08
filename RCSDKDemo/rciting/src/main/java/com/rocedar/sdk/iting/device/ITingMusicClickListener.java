package com.rocedar.sdk.iting.device;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2019-12-10 16:30
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface ITingMusicClickListener {


    void clickPlay();

    void clickPause();

    void clickNext();

    void clickPrevious();

    void clickCheck();

    void upVolume();

    void downVolume();

    void setVolume(int volume);

}

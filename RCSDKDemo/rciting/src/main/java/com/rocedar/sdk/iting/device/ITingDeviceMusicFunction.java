package com.rocedar.sdk.iting.device;

import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2019-12-10 16:24
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface ITingDeviceMusicFunction {

    int[] MUSIC_FUNCTION_TYPE = {
            ResultCallBack.TYPE_DEVICE_SET_VOLUME_INCREASE,
            ResultCallBack.TYPE_DEVICE_SET_VOLUME_REDUCE,
            ResultCallBack.TYPE_DEVICE_SET_VOLUME,
            ResultCallBack.TYPE_DEVICE_CHECK_MUSIC_STATUS,
            ResultCallBack.TYPE_DEVICE_SET_NEXT_SONG,
            ResultCallBack.TYPE_DEVICE_SET_PRE_SONG,
            ResultCallBack.TYPE_DEVICE_SET_PLAY_SONG,
            ResultCallBack.TYPE_DEVICE_SET_PAUSE_SONG
    };

    void sendSongName(boolean musicState, String songName);

    void parsingData(int result, Object[] objects);

    void parsingError(int result);

    void setClickListener(ITingMusicClickListener iTingMusicClick);

}

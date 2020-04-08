package com.rocedar.sdk.iting.device.function;

import android.content.Context;

import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.sdk.iting.device.ITingDeviceMusicFunction;
import com.rocedar.sdk.iting.device.ITingMusicClickListener;

import java.util.Date;

import cn.appscomm.bluetoothsdk.app.BluetoothSDK;
import cn.appscomm.bluetoothsdk.interfaces.ResultCallBack;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2019-12-10 16:15
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCITingDeviceMusicFunction implements ITingDeviceMusicFunction {

    private String TAG = "ITING-Music";

    public Context context;

    private ResultCallBack callBack;

    public RCITingDeviceMusicFunction(Context context, ResultCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    private long lastFunctionTime = -1;


    @Override
    public void sendSongName(boolean musicState, String songName) {
        lastFunctionName = "设置播放音乐名称";
        BluetoothSDK.sendSongName(musicState, songName);

    }

    private String lastFunctionName = "";

    @Override
    public void parsingData(int result, Object[] objects) {
        RCLog.i(TAG, "本次请求[%s]方法开始时间：%d 结束时间：%d 总耗时：%d", lastFunctionName,
                lastFunctionTime, new Date().getTime(), lastFunctionTime - new Date().getTime());
        lastFunctionTime = -1;
        switch (result) {
            case ResultCallBack.TYPE_DEVICE_SET_VOLUME_INCREASE:
                if (iTingMusicClick != null)
                    iTingMusicClick.upVolume();
                break;
            case ResultCallBack.TYPE_DEVICE_SET_VOLUME_REDUCE:
                if (iTingMusicClick != null)
                    iTingMusicClick.downVolume();
                break;
            case ResultCallBack.TYPE_DEVICE_SET_VOLUME:
                if (iTingMusicClick != null)
                    iTingMusicClick.setVolume((int) objects[0]);
                break;
            case ResultCallBack.TYPE_DEVICE_CHECK_MUSIC_STATUS:
                if (iTingMusicClick != null)
                    iTingMusicClick.clickCheck();
                break;
            case ResultCallBack.TYPE_DEVICE_SET_NEXT_SONG:
                if (iTingMusicClick != null)
                    iTingMusicClick.clickNext();
                break;
            case ResultCallBack.TYPE_DEVICE_SET_PRE_SONG:
                if (iTingMusicClick != null)
                    iTingMusicClick.clickPrevious();
                break;
            case ResultCallBack.TYPE_DEVICE_SET_PLAY_SONG:
                if (iTingMusicClick != null)
                    iTingMusicClick.clickPlay();
                break;
            case ResultCallBack.TYPE_DEVICE_SET_PAUSE_SONG:
                if (iTingMusicClick != null)
                    iTingMusicClick.clickPause();
                break;
        }
    }

    @Override
    public void parsingError(int result) {
        switch (result) {

        }
    }

    private ITingMusicClickListener iTingMusicClick;

    @Override
    public void setClickListener(ITingMusicClickListener iTingMusicClick) {
        this.iTingMusicClick = iTingMusicClick;
    }


}

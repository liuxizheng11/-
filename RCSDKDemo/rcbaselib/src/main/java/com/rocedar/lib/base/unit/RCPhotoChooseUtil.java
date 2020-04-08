package com.rocedar.lib.base.unit;

import android.content.Context;

import com.rocedar.lib.base.RCPhotoChooseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/7/12 上午9:00
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCPhotoChooseUtil {

    private Context mContext;

    private ChooseAlbumListener chooseAlbumListen;

    public interface ChooseAlbumListener {
        void over(List<String> chooseList);
    }

    public RCPhotoChooseUtil(Context mContext) {
        this.mContext = mContext;
    }


    public void goChoose(int maxChoose, ChooseAlbumListener chooseAlbumListen) {
        this.chooseAlbumListen = chooseAlbumListen;
        EventBus.getDefault().register(this);
        RCPhotoChooseActivity.goActivity(mContext, maxChoose, false);
    }

    public void goChooseHead(ChooseAlbumListener chooseAlbumListen) {
        this.chooseAlbumListen = chooseAlbumListen;
        EventBus.getDefault().register(this);
        RCPhotoChooseActivity.goActivity(mContext, 1, true);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ArrayList<String> arrayList) {
        if (chooseAlbumListen != null) {
            if (arrayList == null)
                arrayList = new ArrayList<>();
            chooseAlbumListen.over(arrayList);
        }
        EventBus.getDefault().unregister(this);
    }


}

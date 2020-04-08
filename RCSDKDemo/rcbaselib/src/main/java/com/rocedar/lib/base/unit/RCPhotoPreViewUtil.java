package com.rocedar.lib.base.unit;

import android.content.Context;

import com.rocedar.lib.base.RCPhotoPreviewActivity;
import com.rocedar.lib.sdk.rcgallery.dto.RCPhotoDTO;

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
public class RCPhotoPreViewUtil {

    private Context mContext;

    private PreviewListener previewListener;


    public interface PreviewListener {
        void over(List<RCPhotoDTO> chooseList);
    }

    public RCPhotoPreViewUtil(Context mContext) {
        this.mContext = mContext;
    }


    public void goPreview(List<RCPhotoDTO> photoInfo, int chooseIndex, PreviewListener listener) {
        this.previewListener = listener;
        EventBus.getDefault().register(this);
        RCPhotoPreviewActivity.goActivity(mContext, photoInfo, chooseIndex, true);
    }

    public void goPreview(List<String> photoInfo, boolean isNetwork, int chooseIndex, PreviewListener listener) {
        this.previewListener = listener;
        EventBus.getDefault().register(this);
        RCPhotoPreviewActivity.goActivity(mContext, photoInfo, chooseIndex, isNetwork, true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ArrayList<RCPhotoDTO> arrayList) {
        if (previewListener != null) {
            if (arrayList == null)
                arrayList = new ArrayList<>();
            previewListener.over(arrayList);
        }
        EventBus.getDefault().unregister(this);
    }


}

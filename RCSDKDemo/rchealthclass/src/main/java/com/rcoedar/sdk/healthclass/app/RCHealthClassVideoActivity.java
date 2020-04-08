package com.rcoedar.sdk.healthclass.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rcoedar.sdk.healthclass.R;
import com.rcoedar.sdk.healthclass.app.fragment.RCHealthClassVideoFragment;
import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.unit.statuscolor.RCStatusColorHelper;
import com.rocedar.lib.sdk.mediaplayer.video.NiceVideoPlayerManager;

/**
 * 作者：lxz
 * 版本：V1.0
 * 描述：我的健康课堂视频详情页面
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCHealthClassVideoActivity extends RCBaseActivity {
    public static void goActivity(Context context, int infoId) {
        Intent intent = new Intent(context, RCHealthClassVideoActivity.class);
        intent.putExtra("infoId", infoId);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle bundle) {
        setNotAddHead(true);
        super.onCreate(bundle);
        setContentView(R.layout.rc_activity_health_class_video);
        showContent(R.id.rc_activity_health_class_video_framelayout,
                RCHealthClassVideoFragment.newInstance(getIntent().getIntExtra("infoId", -1)));
        RCStatusColorHelper.statusBarLightMode(mContext, true);
    }

    @Override
    public void onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        super.onBackPressed();
    }


}

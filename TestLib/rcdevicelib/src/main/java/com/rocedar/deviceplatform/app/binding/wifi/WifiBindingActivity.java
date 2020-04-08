package com.rocedar.deviceplatform.app.binding.wifi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.R;

/**
 * 作者：lxz
 * 日期：17/2/18 上午11:30
 * 版本：V1.0
 * 描述：Wifi 绑定页面
 * device_id device_id
 * <p>
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class WifiBindingActivity extends RCBaseActivity {

    public static void gotoActivity(Context context, int device_id, String img) {
        Intent intent = new Intent(context, WifiBindingActivity.class);
        intent.putExtra("device_id", device_id);
        intent.putExtra("img", img);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_binding_main);
        mRcHeadUtil.setLeftBack().setTitle(getString(R.string.rcdevice_my_device_wifi).toUpperCase());
        showBodyFatThree();
    }


    /**
     * 跳转到 体脂第三步骤
     */
    public void showBodyFatThree() {
        showContent(R.id.wifi_main_framelayout, WifiThreeFragment.newInstance(
                getIntent().getIntExtra("device_id", -1), getIntent().getStringExtra("img")), null);
    }
}

package app.phj.com.testlib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.base.manger.RCBaseActivity;

import app.phj.com.testlib.device.AppDeviceFragment;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/20 下午6:46
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class AppDeviceActivity extends RCBaseActivity {

    public static void goActivity(Context context) {
        context.startActivity(new Intent(context, AppDeviceActivity.class));
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        showContent(R.id.frame_content, new AppDeviceFragment(), null);
    }

}

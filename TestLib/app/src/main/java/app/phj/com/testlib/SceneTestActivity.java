package app.phj.com.testlib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.app.scene.RCSceneUtil;
import com.rocedar.deviceplatform.app.scene.SceneType;

/**
 * 项目名称：TestLib
 * <p>
 * 作者：phj
 * 日期：2017/7/24 下午2:21
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class SceneTestActivity extends RCBaseActivity {

    private static final String KEY_TYPE = "type";
    private static final String KEY_SCENE_TYPE = "scene_type";
    private static final String KEY_SCENE_DEVICE = "device_id";
    private static final int VALUE_TYPE_CHOOSE = 0;
    private static final int VALUE_TYPE_DEVICE = 1;
    private static final int VALUE_TYPE_INFO = 2;


    public static void goActivityChooseType(Context context) {
        Intent intent = new Intent(context, SceneTestActivity.class);
        intent.putExtra(KEY_TYPE, VALUE_TYPE_CHOOSE);
        context.startActivity(intent);
    }

    public static void goActivityChooseDevice(Context context, SceneType chooseType) {
        Intent intent = new Intent(context, SceneTestActivity.class);
        intent.putExtra(KEY_TYPE, VALUE_TYPE_DEVICE);
        intent.putExtra(KEY_SCENE_TYPE, chooseType);
        context.startActivity(intent);
    }

    public static void goActivityInfo(Context context, SceneType chooseType, int chooseDeviceId) {
        Intent intent = new Intent(context, SceneTestActivity.class);
        intent.putExtra(KEY_TYPE, VALUE_TYPE_INFO);
        intent.putExtra(KEY_SCENE_TYPE, chooseType);
        intent.putExtra(KEY_SCENE_DEVICE, chooseDeviceId);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        if (RCSceneUtil.isDoSceneIn()) {
            Bundle bundle2 = new Bundle();
            bundle2.putSerializable("type", RCSceneUtil.doSceneType());
            bundle2.putInt("device", RCSceneUtil.doSceneDeviceId());
            showContent(R.id.frame_content, new SceneTestInfoFragment(), bundle2);
        } else {
            switch (getIntent().getIntExtra(KEY_TYPE, VALUE_TYPE_CHOOSE)) {
                case VALUE_TYPE_CHOOSE:
                    showContent(R.id.frame_content, new SceneTestChooseTypeFragment(), null);
                    break;
                case VALUE_TYPE_DEVICE:
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("type", getIntent().getSerializableExtra(KEY_SCENE_TYPE));
                    showContent(R.id.frame_content, new SceneTestChooseDeviceFragment(), bundle);
                    break;
                case VALUE_TYPE_INFO:
                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable("type", getIntent().getSerializableExtra(KEY_SCENE_TYPE));
                    bundle2.putInt("device", getIntent().getIntExtra(KEY_SCENE_DEVICE, -1));
                    showContent(R.id.frame_content, new SceneTestInfoFragment(), bundle2);
                    break;
            }
        }
    }


}

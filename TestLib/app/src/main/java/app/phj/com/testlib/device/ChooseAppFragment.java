package app.phj.com.testlib.device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rocedar.base.RCBaseConfig;
import com.rocedar.base.manger.RCBaseFragment;
import com.rocedar.deviceplatform.app.binding.wifi.WifiBindingActivity;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;

import app.phj.com.testlib.FunctionActivity;
import app.phj.com.testlib.LoginFragment;
import app.phj.com.testlib.R;
import app.phj.com.testlib.SceneTestActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/14 上午11:57
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class ChooseAppFragment extends RCBaseFragment {


    @BindView(R.id.choose_app_1)
    Button chooseApp1;
    @BindView(R.id.choose_app_2)
    Button chooseApp2;
    @BindView(R.id.button_01)
    Button button01;
    Unbinder unbinder;
    @BindView(R.id.button_02)
    Button button02;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_app, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @OnClick({R.id.choose_app_1, R.id.choose_app_2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_app_1:
                RCBaseConfig.APPTAG = "101";
                break;
            case R.id.choose_app_2:
                RCBaseConfig.APPTAG = "102";
                break;
        }
        if (mActivity instanceof FunctionActivity) {
            ((FunctionActivity) mActivity).showContent(
                    R.id.frame_content, new LoginFragment(), null
            );
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.button_01)
    public void onViewClicked() {
//        mActivity.startActivity(new Intent(mActivity, RCBluetoothImplTest.class));
        WifiBindingActivity.gotoActivity(mActivity, RCDeviceDeviceID.LX_WIFI_BP, "");
    }

    @OnClick(R.id.button_02)
    public void goSceneTest() {
        SceneTestActivity.goActivityChooseType(mActivity);
    }
}

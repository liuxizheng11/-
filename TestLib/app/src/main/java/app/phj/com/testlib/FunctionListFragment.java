package app.phj.com.testlib;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.rocedar.base.RCLog;
import com.rocedar.base.manger.RCBaseFragment;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;
import com.rocedar.deviceplatform.app.devicelist.DeviceChooseListActivity;
import com.rocedar.deviceplatform.app.devicelist.DeviceFunctionListActivity;
import com.rocedar.deviceplatform.device.bluetooth.impl.dudo.DBDeviceDuDoData;

import app.phj.com.testlib.device.ChooseAppFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/14 下午12:32
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class FunctionListFragment extends RCBaseFragment {


    @BindView(R.id.function_list_01)
    Button functionList01;
    @BindView(R.id.function_list_02)
    Button functionList02;
    @BindView(R.id.function_input)
    EditText functionInput;
    @BindView(R.id.function_list_03)
    Button functionList03;
    @BindView(R.id.function_list_04)
    Button functionList04;
    @BindView(R.id.function_list_05)
    Button functionList05;
    @BindView(R.id.function_list_07)
    Button functionList07;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_function_list, null);
        ButterKnife.bind(this, view);
        functionList03.setEnabled(false);
        functionInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 3) {
                    functionList03.setEnabled(true);
                } else {
                    functionList03.setEnabled(false);
                }
            }
        });
        dbDeviceDuDoData = new DBDeviceDuDoData(mActivity);
//        functionList08.setText(functionList08.getText()+ GlideUtil.getTotalCacheSize(mActivity));
        return view;
    }

    private DBDeviceDuDoData dbDeviceDuDoData;

    @OnClick({R.id.function_list_01, R.id.function_list_02, R.id.function_list_03,
            R.id.function_list_04, R.id.function_list_05, R.id.function_list_07})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.function_list_01:
                DeviceChooseListActivity.goActivity(mActivity);
                break;
            case R.id.function_list_02:
                DeviceFunctionListActivity.gotoActivity(mActivity);
                break;
            case R.id.function_list_03:
//                int text;
//                try {
//                    text = Integer.parseInt(functionInput.getText().toString().trim());
//                    DeviceFunctionListActivity.gotoActivity(mActivity, text);
//                } catch (NumberFormatException e) {
//                    RCToast.Center(mActivity, "输入异常");
//                }
                RCLog.e("TestLib", dbDeviceDuDoData.getHistorySleepNew().toString());
                break;
            case R.id.function_list_04:
                AppDeviceActivity.goActivity(mActivity);
                break;
            case R.id.function_list_05:
                startActivity(new Intent(mActivity, LogActivity.class));
//                startActivity(new Intent(mActivity, WifiBindingActivity.class));
//                WifiBindingActivity.gotoActivity(mActivity, RCDeviceDeviceID.LX_WIFI_BP);
                break;
            case R.id.function_list_07:
                if (mActivity instanceof FunctionActivity) {
                    RCSPBaseInfo.loginOut();
                    ((FunctionActivity) mActivity).showContent(
                            R.id.frame_content, new ChooseAppFragment(), null
                    );
                }
                break;

        }
    }
}

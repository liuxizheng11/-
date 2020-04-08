package app.phj.com.testlib.device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rocedar.base.manger.RCBaseFragment;
import com.rocedar.deviceplatform.LogUnit;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.device.phone.RCPhoneStepUtil;
import com.rocedar.deviceplatform.device.phone.listener.RCPhonePedometerListener;
import com.rocedar.deviceplatform.dto.device.RCDeviceStepDataDTO;
import com.rocedar.deviceplatform.unit.DateUtil;

import org.json.JSONArray;

import app.phj.com.testlib.R;
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

public class DevicePhoneTestFragment extends RCBaseFragment implements RCPhonePedometerListener {


    @BindView(R.id.info1)
    TextView info1;
    @BindView(R.id.function_text)
    TextView functionText;
    @BindView(R.id.info2)
    TextView info2;
    @BindView(R.id.function_list_01)
    Button functionList01;
    @BindView(R.id.function_list_02)
    Button functionList02;
    @BindView(R.id.function_list_03)
    Button functionList03;
    @BindView(R.id.function_list_06)
    Button functionList06;



    private int deviceId = RCDeviceDeviceID.Phone;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_test_phone, null);
        ButterKnife.bind(this, view);
        rcPhoneStepUtil = RCPhoneStepUtil.getInstance(mActivity);
        info1.setText("设备名称：" + LogUnit.getDeviceName(deviceId));
        return view;
    }


    private RCPhoneStepUtil rcPhoneStepUtil;

    @OnClick({R.id.info1, R.id.info2, R.id.function_list_01, R.id.function_list_02, R.id.function_list_03, R.id.function_list_06})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.function_list_01:
                stepChange(rcPhoneStepUtil.calculationTodaySteps());
                break;
            case R.id.function_list_02:
                rcPhoneStepUtil.setRcPhonePedometerListener(this);
                break;
            case R.id.function_list_03:
                rcPhoneStepUtil.setRcPhonePedometerListener(null);
                break;

        }
    }

    private int doType;


    private void showLog(final String info) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                functionText.setText(LogUnit.logString(info, LogUnit.getDeviceName(deviceId))
                        + "\n" + functionText.getText());
            }
        });
    }


    public void dataInfo(JSONArray array) {
        showLog(LogUnit.logStringGetDataOk(deviceId, doType, array.toString()));
    }


    @Override
    public void stepChange(int step) {
        JSONArray array = new JSONArray();
        RCDeviceStepDataDTO stepDataDTO = new RCDeviceStepDataDTO();
        stepDataDTO.setStep(step);
        stepDataDTO.setDeviceId(RCDeviceDeviceID.Phone);
        stepDataDTO.setDate(Long.parseLong(DateUtil.getFormatNow("yyyyMMdd") + "000000"));
        array.put(stepDataDTO.getJSON());
        dataInfo(array);
    }
}

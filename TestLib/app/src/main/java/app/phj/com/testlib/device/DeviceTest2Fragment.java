package app.phj.com.testlib.device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rocedar.base.manger.RCBaseFragment;
import com.rocedar.deviceplatform.app.RCGetBuletoothData;
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
 * 日期：2017/3/25 下午10:23
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class DeviceTest2Fragment extends RCBaseFragment {


    @BindView(R.id.fragment_test2_device_b1)
    Button fragmentTest2DeviceB1;
    @BindView(R.id.fragment_test2_device_b2)
    Button fragmentTest2DeviceB2;
    @BindView(R.id.fragment_test2_device_info)
    TextView fragmentTest2DeviceInfo;


    private RCGetBuletoothData rcGetBuletoothData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test2_device, null);
        rcGetBuletoothData = new RCGetBuletoothData(mActivity);
        rcGetBuletoothData.setGetRealTimeDataListener(new RCGetBuletoothData.GetBluetoothDataListener() {
            @Override
            public void getDataOver(final JSONArray info) {
                mRcHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        fragmentTest2DeviceInfo.setText(
                                DateUtil.getFormatNow("HH:mm:ss") + "\n" +
                                        info.toString() + "\n" + fragmentTest2DeviceInfo.getText()

                        );
                    }
                });
            }
        });
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.fragment_test2_device_b1, R.id.fragment_test2_device_b2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_test2_device_b1:
                rcGetBuletoothData.startGetData();
                break;
            case R.id.fragment_test2_device_b2:
                rcGetBuletoothData.stopGetData();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rcGetBuletoothData.doDestroyDevice();
    }
}

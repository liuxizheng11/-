package app.phj.com.testlib.device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rocedar.base.manger.RCBaseFragment;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.dto.data.RCDeviceAlreadyBindDTO;
import com.rocedar.deviceplatform.sharedpreferences.RCSPDeviceInfo;

import java.util.List;

import app.phj.com.testlib.AppDeviceActivity;
import app.phj.com.testlib.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/20 下午6:50
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class AppDeviceFragment extends RCBaseFragment {

    @BindView(R.id.device_app_1)
    Button deviceApp1;
    @BindView(R.id.device_app_2)
    Button deviceApp2;
    @BindView(R.id.device_app_3)
    Button deviceApp3;
    @BindView(R.id.device_app_4)
    Button deviceApp4;
    @BindView(R.id.device_app_5)
    Button deviceApp5;
    @BindView(R.id.device_app_6)
    Button deviceApp6;


    private List<RCDeviceAlreadyBindDTO> alreadyBindDTOs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_device_list, null);
        ButterKnife.bind(this, view);

        alreadyBindDTOs = RCSPDeviceInfo.getBlueToothInfo();
        if (alreadyBindDTOs != null) {
            for (int i = 0; i < alreadyBindDTOs.size(); i++) {
                switch (alreadyBindDTOs.get(i).getDevice_id()) {
                    case RCDeviceDeviceID.BZL:
                        deviceApp1.setEnabled(true);
                        macBZL = alreadyBindDTOs.get(i).getDevice_no();
                        break;
                    case RCDeviceDeviceID.MJK_ANDROID:
                        macMJK = alreadyBindDTOs.get(i).getDevice_no();
                        deviceApp2.setEnabled(true);
                        break;
                    case RCDeviceDeviceID.YD:
                        macYD = alreadyBindDTOs.get(i).getDevice_no();
                        deviceApp3.setEnabled(true);
                        break;
                    case RCDeviceDeviceID.HEHAQI:
                        macHEHAQi = alreadyBindDTOs.get(i).getDevice_no();
                        deviceApp4.setEnabled(true);
                        break;
                    case RCDeviceDeviceID.HF_DUDO:
                        macDUDO = alreadyBindDTOs.get(i).getDevice_no();
                        deviceApp6.setEnabled(true);
                        break;
                }
            }
        }
        return view;
    }

    private String macBZL = "";
    private String macMJK = "";
    private String macYD = "";
    private String macHEHAQi = "";
    private String macDUDO = "";


    @OnClick({R.id.device_app_1, R.id.device_app_2, R.id.device_app_3, R.id.device_app_4, R.id.device_app_5,R.id.device_app_6})
    public void onClick(View view) {
        if (mActivity instanceof AppDeviceActivity) {
            Bundle bundle = new Bundle();
            switch (view.getId()) {
                case R.id.device_app_1:
                    bundle.putString("mac", macBZL);
                    ((AppDeviceActivity) mActivity).showContent(
                            R.id.frame_content, new DeviceBZLTestFragment(), bundle
                    );
                    break;
                case R.id.device_app_2:
                    bundle.putString("mac", macMJK);
                    ((AppDeviceActivity) mActivity).showContent(
                            R.id.frame_content, new DeviceMJKTestFragment(), bundle
                    );
                    break;
                case R.id.device_app_3:
                    bundle.putString("mac", macYD);
                    ((AppDeviceActivity) mActivity).showContent(
                            R.id.frame_content, new DeviceYDTestFragment(), bundle
                    );
                    break;
                case R.id.device_app_4:
                    bundle.putString("mac", macHEHAQi);
                    ((AppDeviceActivity) mActivity).showContent(
                            R.id.frame_content, new DeviceHeHaQiTestFragment(), bundle
                    );
                    break;
                case R.id.device_app_5:
                    ((AppDeviceActivity) mActivity).showContent(
                            R.id.frame_content, new DevicePhoneTestFragment(), null
                    );
                    break;
                case R.id.device_app_6:
                    bundle.putString("mac", macDUDO);
                    ((AppDeviceActivity) mActivity).showContent(
                            R.id.frame_content, new DeviceDUDOTestFragment(), bundle
                    );
                    break;
            }
        }
    }
}

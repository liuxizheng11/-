package app.phj.com.testlib;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rocedar.base.manger.RCBaseFragment;
import com.rocedar.deviceplatform.app.scene.SceneType;
import com.rocedar.deviceplatform.config.RCBluetoothDoType;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.device.bluetooth.RCBlueTooth;
import com.rocedar.deviceplatform.device.bluetooth.impl.RCBluetoothBongImpl;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;

import org.json.JSONArray;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 项目名称：TestLib
 * <p>
 * 作者：phj
 * 日期：2017/7/24 下午3:27
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class SceneTestChooseDeviceFragment extends RCBaseFragment {


    Unbinder unbinder;
    @BindView(R.id.device_status)
    TextView deviceStatus;
    @BindView(R.id.button_lxjs)
    Button buttonLxjs;
    @BindView(R.id.button_tbsj)
    Button buttonTbsj;
    @BindView(R.id.button_kaishi)
    Button buttonKaishi;
    @BindView(R.id.button_tiaoguo)
    Button buttonTiaoguo;


    private RCBlueTooth rcBlueTooth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_scene_choose_device, null);
        unbinder = ButterKnife.bind(this, view);
        rcBlueTooth = RCBluetoothBongImpl.getInstance(mActivity, RCDeviceDeviceID.BONG_2PH);
        deviceStatus.setText(rcBlueTooth.isConnect() ? "已连接" : "未连接");
        if (rcBlueTooth.isConnect()) {
            buttonLxjs.setEnabled(false);
            buttonTbsj.setEnabled(true);
        } else {
            buttonLxjs.setEnabled(true);
            buttonTbsj.setEnabled(false);
        }
        buttonKaishi.setEnabled(false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private String mac = "CD:3D:CD:F5:9E:83";


    @OnClick({R.id.button_lxjs, R.id.button_tbsj, R.id.button_kaishi, R.id.button_tiaoguo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_lxjs:
                rcBlueTooth.sendInstruct(new RCBluetoothGetDataListener() {
                    @Override
                    public void getDataError(int status, String msg) {

                    }

                    @Override
                    public void getDataStart() {

                    }

                    @Override
                    public void dataInfo(JSONArray array) {
                        buttonLxjs.setEnabled(false);
                        buttonTbsj.setEnabled(true);
                        deviceStatus.setText(rcBlueTooth.isConnect() ? "已连接" : "未连接");
                    }
                }, mac, RCBluetoothDoType.DO_BINDING);
                break;
            case R.id.button_tbsj:
                rcBlueTooth.sendInstruct(new RCBluetoothGetDataListener() {
                    @Override
                    public void getDataError(int status, String msg) {

                    }

                    @Override
                    public void getDataStart() {

                    }

                    @Override
                    public void dataInfo(JSONArray array) {
                        buttonKaishi.setEnabled(true);
                    }
                }, mac, RCBluetoothDoType.DO_SYNC);
                break;
            case R.id.button_kaishi:
                SceneTestActivity.goActivityInfo(mActivity,
                        (SceneType) getArguments().getSerializable("type"), RCDeviceDeviceID.BONG_2PH);
                break;
            case R.id.button_tiaoguo:
                SceneTestActivity.goActivityInfo(mActivity,
                        (SceneType) getArguments().getSerializable("type"), -1);
                break;
        }
    }
}

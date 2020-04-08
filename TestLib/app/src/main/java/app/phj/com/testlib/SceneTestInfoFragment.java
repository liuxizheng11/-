package app.phj.com.testlib;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rocedar.base.manger.RCBaseFragment;
import com.rocedar.deviceplatform.app.scene.RCSceneUtil;
import com.rocedar.deviceplatform.app.scene.SceneSPInfo;
import com.rocedar.deviceplatform.app.scene.SceneStatus;
import com.rocedar.deviceplatform.app.scene.SceneType;
import com.rocedar.deviceplatform.config.RCBluetoothDataType;
import com.rocedar.deviceplatform.config.RCBluetoothDoType;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.config.RCDeviceIndicatorID;
import com.rocedar.deviceplatform.db.bong.DBDataBongHeart;
import com.rocedar.deviceplatform.device.bluetooth.RCBlueTooth;
import com.rocedar.deviceplatform.device.bluetooth.impl.RCBluetoothBongImpl;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothGetDataListener;
import com.rocedar.deviceplatform.unit.DateUtil;

import org.json.JSONArray;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

public class SceneTestInfoFragment extends RCBaseFragment {

    private String TAG = "SceneTestInfoFragment";


    public final static int CHECK_STATUS = 0;

    Unbinder unbinder;
    @BindView(R.id.text_device_status)
    TextView textDeviceStatus;
    @BindView(R.id.ssxl)
    TextView ssxl;
    @BindView(R.id.button_zt)
    Button buttonZt;
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.cjlx)
    TextView cjlx;
    @BindView(R.id.kssj)
    TextView kssj;
    @BindView(R.id.cxsj)
    TextView cxsj;
    @BindView(R.id.button_lxjs)
    Button buttonLxjs;
    @BindView(R.id.button_kshqssxl)
    Button buttonKshqssxl;
    @BindView(R.id.button_js)
    Button buttonJs;
    @BindView(R.id.cjzt)
    TextView cjzt;

    //bong2PH
    private String mac = "CD:3D:CD:F5:9E:83";
    //bong4
//    private String mac = "C7:FD:57:6C:4F:51";

    private RCBlueTooth rcBlueTooth;

    private Timer timer = new Timer();

    private SceneType mSceneType;

    private int deviceId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_scene, null);
        unbinder = ButterKnife.bind(this, view);
        mSceneType = (SceneType) getArguments().getSerializable("type");
//        deviceId = getArguments().getInt("device");
        deviceId = RCDeviceDeviceID.BONG_2PH;
        cjlx.setText(mSceneType.name());
        rcBlueTooth = RCBluetoothBongImpl.getInstance(mActivity, deviceId);
        if (!RCSceneUtil.isDoSceneIn()) {
            doStart();
        } else {
            List<SceneSPInfo.SceneStartDataDTO> info = SceneSPInfo.getValueInfo();
            setInfo("上次运动没有结束，类型：" + mSceneType.name() + "，数据条数：" + info.size());
            for (int i = 0; i < info.size(); i++) {
                setInfo("数据" + i + ":" +
                        "开始时间：" + info.get(i).getStart_time() +
                        " 开始步数："+ info.get(i).getStart_step()+
                        "结束时间：" + info.get(i).getStop_time() +
                        " 结束步数："+ info.get(i).getStop_step());
            }
        }
        reConnect();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = CHECK_STATUS;
                handler.sendMessage(message);
            }
        }, 2000);
        return view;
    }

    private void doStart() {
        rcBlueTooth.sendInstruct(new RCBluetoothGetDataListener() {
            @Override
            public void getDataError(int status, String msg) {

            }

            @Override
            public void getDataStart() {

            }

            @Override
            public void dataInfo(JSONArray array) {
                saveStatusAndData(SceneStatus.START);
            }
        }, mac, RCBluetoothDoType.DO_GET_REALTIME_RUN_START);
    }

    private void doStop() {
        rcBlueTooth.sendInstruct(new RCBluetoothGetDataListener() {
            @Override
            public void getDataError(int status, String msg) {

            }

            @Override
            public void getDataStart() {

            }

            @Override
            public void dataInfo(JSONArray array) {
                saveStatusAndData(SceneStatus.STOP);

            }
        }, mac, RCBluetoothDoType.DO_GET_REALTIME_RUN_STOP);

    }

    /**
     * 保存场景状态和数据
     *
     * @param sceneStatus
     */
    private void saveStatusAndData(final SceneStatus sceneStatus) {
        final String time = DateUtil.getFormatNow("yyyyMMddHHmmss");//当前时间
        cjzt.setText(sceneStatus.name());
        switch (sceneStatus) {
            case STOP:
                SceneSPInfo.doStop(mSceneType);
                break;
            case PAUSE:
                SceneSPInfo.doPause(mSceneType);
                break;
            case START:
                if (SceneSPInfo.getLastSceneStatus() != SceneStatus.PAUSE) {
                    kssj.setText(time);
                }
                SceneSPInfo.doStart(mSceneType, deviceId);
                break;
        }
        //先同步，再获取步数
        rcBlueTooth.sendInstruct(new RCBluetoothGetDataListener() {
            @Override
            public void getDataError(int status, String msg) {

            }

            @Override
            public void getDataStart() {

            }

            @Override
            public void dataInfo(JSONArray array) {
                rcBlueTooth.sendInstruct(new RCBluetoothGetDataListener() {
                    @Override
                    public void getDataError(int status, String msg) {

                    }

                    @Override
                    public void getDataStart() {

                    }

                    @Override
                    public void dataInfo(JSONArray array) {
                        int step = 0;
                        if (array != null && array.length() > 0)
                            step = array.optJSONObject(0).optJSONObject("value").optInt(RCDeviceIndicatorID.STEP + "");
                        switch (sceneStatus) {
                            case STOP:
                                if (SceneSPInfo.getLastSceneStatus() != SceneStatus.PAUSE) {
                                    SceneSPInfo.saveSceneStopData(time, step);
                                }
                                setInfo("结束" + mSceneType.name() + "，步数" + step + "，时间" + time);
                                setInfo("mSceneType.name()数据为：" + SceneSPInfo.getSceneData(deviceId, mac, new DBDataBongHeart(mActivity)).toString());
                                break;
                            case PAUSE:
                                SceneSPInfo.saveSceneStopData(time, step);
                                setInfo("暂停" + mSceneType.name() + "，步数" + step + "，时间" + time);
                                break;
                            case START:
                                if (SceneSPInfo.getLastSceneStatus() == SceneStatus.PAUSE) {
                                    SceneSPInfo.saveSceneStartData(time, step);
                                    setInfo("恢复" + mSceneType.name() + "，步数" + step + "，时间" + time);
                                } else {
                                    setInfo("开始" + mSceneType.name() + "，步数" + step + "，时间" + time);
                                    SceneSPInfo.saveSceneStartData(time, step);
                                }
                                break;
                        }

                    }
                }, mac, RCBluetoothDataType.DATATYPE_STEP_TODAY);
            }
        }, mac, RCBluetoothDoType.DO_SYNC);
    }



    /**
     * 开始获取实时心率
     */
    private void startGetHeart() {
        rcBlueTooth.sendInstruct(new RCBluetoothGetDataListener() {
            @Override
            public void getDataError(int status, String msg) {

            }

            @Override
            public void getDataStart() {

            }

            @Override
            public void dataInfo(JSONArray array) {
                int heartRate = 0;
                if (array.length() >= 0) {
                    heartRate = array.optJSONObject(0).optJSONObject("value").optInt(RCDeviceIndicatorID.Heart_Rate + "");
                }
                ssxl.setText(heartRate + "");
            }
        }, mac, RCBluetoothDoType.DO_TEST_HEART_RATE_START);
    }

    private void setInfo(final String infoMsg) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                info.setText(infoMsg + "\n" + info.getText());
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private boolean connectIn = false;

    private boolean isStartSSXL = false;


    private void reSSXL(boolean isConnect) {
        if (isConnect)
            if (isStartSSXL) {
                buttonKshqssxl.setEnabled(false);
                buttonKshqssxl.setText("正在监听心率");
            } else {
                buttonKshqssxl.setEnabled(true);
                buttonKshqssxl.setText("开始实时心率");
            }
        else {
            buttonKshqssxl.setText("开始实时心率(不可用)");
            buttonKshqssxl.setEnabled(false);
        }
    }

    private void reConnect() {
        textDeviceStatus.setText(rcBlueTooth.isConnect() ? "已连接" : "未连接");
        if (rcBlueTooth.isConnect()) {
            reSSXL(true);
            buttonLxjs.setEnabled(false);
        } else {
            reSSXL(false);
            if (connectIn) return;
            buttonLxjs.setEnabled(true);
            connectIn = true;
            rcBlueTooth.sendInstruct(new RCBluetoothGetDataListener() {
                @Override
                public void getDataError(int status, String msg) {

                }

                @Override
                public void getDataStart() {

                }

                @Override
                public void dataInfo(JSONArray array) {
                    connectIn = false;
                    buttonLxjs.setEnabled(false);
                    reSSXL(true);
                }
            }, mac, RCBluetoothDoType.DO_SYNC);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHECK_STATUS:
                    reConnect();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @OnClick({R.id.button_lxjs, R.id.button_kshqssxl, R.id.button_zt, R.id.button_js})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_lxjs:
                reConnect();
                break;
            case R.id.button_kshqssxl:
                startGetHeart();
                break;
            case R.id.button_zt:
                if (SceneSPInfo.getLastSceneStatus() == SceneStatus.START) {//进行中
                    saveStatusAndData(SceneStatus.PAUSE);
                    buttonZt.setText("恢复");
                } else if (SceneSPInfo.getLastSceneStatus() == SceneStatus.PAUSE) {//暂停
                    saveStatusAndData(SceneStatus.START);
                    buttonZt.setText("暂停");
                }
                break;
            case R.id.button_js:
                doStop();
                buttonKshqssxl.setEnabled(false);
                buttonLxjs.setEnabled(false);
                break;
        }
    }
}

package app.phj.com.testlib;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.rocedar.base.RCBaseManage;
import com.rocedar.base.RCLog;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;
import com.rocedar.deviceplatform.dto.data.RCDeviceAlreadyBindDTO;
import com.rocedar.deviceplatform.request.RCDeviceDataRequest;
import com.rocedar.deviceplatform.request.impl.RCDeviceOperationRequestImpl;
import com.rocedar.deviceplatform.request.listener.RCDeviceAlreadyBindListener;
import com.rocedar.deviceplatform.sharedpreferences.RCSPDeviceInfo;

import java.util.ArrayList;
import java.util.List;

import app.phj.com.testlib.device.ChooseAppFragment;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/14 上午10:42
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class FunctionActivity extends RCBaseActivity {

    private String TAG = "FunctionActivity";

    private RCDeviceDataRequest deviceOperationRequest;

    @Override
    protected void onStart() {
        super.onStart();
        //需要在使用前初始化
        RCBaseManage.getInstance().init(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        deviceOperationRequest = RCDeviceOperationRequestImpl.getInstance(mContext);

//        int sport_start_time = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()); //(int) (System.currentTimeMillis() / 1000);
//        RCLog.i(TAG, "sport_start_time:" + sport_start_time);
//        RCLog.i(TAG, "time:" + System.currentTimeMillis());
//        RCLog.i(TAG, "date:" + new Date().getTime());
        List<SceneStartDataDTO> dtos = new ArrayList<>();
        SceneStartDataDTO dto = new SceneStartDataDTO();
        dto.setStart_step(123);
        dto.setStart_time("20170724171717");
        dtos.add(dto);
        String temp = new Gson().toJson(dtos);
        RCLog.i(TAG, temp);
        List<SceneStartDataDTO> list = null;
        try {
            list = new Gson().fromJson(temp, new TypeToken<List<SceneStartDataDTO>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            RCLog.e(TAG, "解析出错");
        }
//        RCLog.i(TAG, "数据长度：" + list.size());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!RCSPBaseInfo.isLogin()) {
            showContent(R.id.frame_content, new ChooseAppFragment(), null);
        } else {
            showContent(
                    R.id.frame_content, new FunctionListFragment(), null
            );

        }
    }

    public void showContent(int layoutId, Fragment fragment, Bundle bundle) {
        if (bundle != null)
            fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(layoutId, fragment, fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();
        if (RCSPBaseInfo.isLogin()) {
            deviceOperationRequest.queryDeviceAlreadyBindList(new RCDeviceAlreadyBindListener() {
                @Override
                public void getDataSuccess(List<RCDeviceAlreadyBindDTO> dtoList) {
                    RCSPDeviceInfo.saveBlueToothInfo(dtoList);
                }

                @Override
                public void getDataError(int status, String msg) {

                }
            });
        }
    }

    private static class SceneStartDataDTO {

        private String start_time;
        private int start_step;
        private String stop_time = "";
        private int stop_step = -1;


        public int getStart_step() {
            return start_step;
        }

        public void setStart_step(int start_step) {
            this.start_step = start_step;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getStop_time() {
            return stop_time;
        }

        public void setStop_time(String stop_time) {
            this.stop_time = stop_time;
        }

        public int getStop_step() {
            return stop_step;
        }

        public void setStop_step(int stop_step) {
            this.stop_step = stop_step;
        }
    }
}

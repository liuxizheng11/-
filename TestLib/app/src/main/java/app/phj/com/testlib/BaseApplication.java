package app.phj.com.testlib;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.view.WindowManager;

import com.rocedar.base.RCBaseConfig;
import com.rocedar.base.RCBaseManage;
import com.rocedar.base.network.RequestCode;
import com.rocedar.base.network.RequestDataErrorLister;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/20 下午9:41
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class BaseApplication extends MultiDexApplication {


    private static BaseApplication sInstance;

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized BaseApplication getInstance() {
        return sInstance;
    }


//    private String pt = "http://192.168.18.22:38080";
//    private String dy = "http://192.168.18.25/rest/3.0/";
//    private String pt = "http://192.168.18.25/";

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        WindowManager wm = (WindowManager) getInstance()
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        int w = wm.getDefaultDisplay().getWidth();
        RCBaseManage.getInstance().init(sInstance).setRequestDataErrorLister(new RequestDataErrorLister() {
            @Override
            public void error(int code, String msg) {
                if (code == RequestCode.STATUS_CODE_LOGIN_ERROR
                        || code == RequestCode.STATUS_CODE_LOGIN_OUT) {
                    RCSPBaseInfo.loginOut();
                    RCBaseManage.getScreenManger().currentActivity().startActivity(new Intent(
                            RCBaseManage.getScreenManger().currentActivity(),
                            FunctionActivity.class
                    ));
                }
            }
        });
        RCBaseConfig.setAPPTag(RCBaseConfig.APPTAG_N3);
        RCBaseConfig.setNetWorkUrl("http://www.rocedar.cn:8002");
        RCBaseConfig.setPTNetWorkUrl("http://www.rocedar.cn:8002");
//        RCBaseConfig.setNetWorkUrl("http://192.168.18.21/");
        RCBaseConfig.setIsDebug(true);
//        RCBaseConfig.setPTNetWorkUrl("http://192.168.18.25/");
        RCBaseConfig.setWWZIstest(true);
        Intent localService = new Intent(this, TestService.class);
        this.startService(localService);
    }


}

package sdk.lib.rocedar.com.rcsdkdemo.manager;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.rocedar.lib.base.config.Config;
import com.rocedar.lib.base.config.RCBaseConfig;
import com.rocedar.lib.base.manage.RCSDKManage;
import com.rocedar.lib.base.network.IRCDataErrorLister;
import com.rocedar.lib.base.network.IRCRequestCode;
import com.rocedar.lib.base.network.IRequestCode;
import com.rocedar.lib.base.network.IResponseData;
import com.rocedar.lib.base.network.NetworkMethod;
import com.rocedar.lib.base.network.RCRequestUtil;
import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.sdk.scanner.RCScannerManager;
import com.rocedar.lib.sdk.share.RCUmeng;
import com.rocedar.sdk.familydoctor.RCFD;
import com.rocedar.sdk.iting.ITingManage;
import com.rocedar.sdk.shop.config.RCShopConfigUtil;

import org.json.JSONObject;

import sdk.lib.rocedar.com.rcsdkdemo.TestLoginActivity;
import sdk.lib.rocedar.com.rcsdkdemo.rcconfig.UserChooseConfig;
import sdk.lib.rocedar.com.rcsdkdemo.rcconfig.WebViewConfig;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/7/17 上午10:43
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class BaseApplication extends Application {


    public static String weixinID = "wxdacc6f451141f627";
    public static String weixinSecret = "53d85d3bfc06d21f275bd96afb73143a";
    public static String qqID = "1104610967";
    public static String qqKey = "ejYsTxjwn81vsCEl";

            public static final String APP_NETWORK_URL = "http://dongya.rocedar.com/rest/3.0/";
//    public static final String APP_NETWORK_URL = "http://192.168.18.25/rest/3.0/";


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        ITingManage.init(this);
        super.onCreate();
        //初始化BaseLib
        RCSDKManage.getInstance().init(this);

        //（*基础库配置）将API的请求地址设置到平台，用于webview打开时的校验
        RCBaseConfig.setNetWorkConfig(NetworkMethod.API, APP_NETWORK_URL);
        RCRequestUtil.setCodeConfig(IRequestCode.STATUS_CODE_LOGIN_OUT);
        RCRequestUtil.setCodeConfig(IRequestCode.STATUS_CODE_LOGIN_ERROR);
        RCRequestUtil.setCodeConfig(IRequestCode.STATUS_CODE_LOGIN_OUT_FORCE);
        RCSDKManage.getInstance().setRequestDataErrorLister(new IRCDataErrorLister() {
            @Override
            public void error(Context context, int code, String msg) {
                switch (code) {
                    case IRequestCode.STATUS_CODE_LOGIN_OUT:
                    case IRequestCode.STATUS_CODE_LOGIN_ERROR:
                    case IRequestCode.STATUS_CODE_LOGIN_OUT_FORCE:
                        TestLoginActivity.goActivity(context);
                        break;
                    case IRCRequestCode.STATUS_APP_CODE_TOKEN_OVERDUE:
                        //token失效
                        TestLoginActivity.loginSDK(context, new IResponseData() {
                            @Override
                            public void getDataSucceedListener(JSONObject jsonObject) {
                                RCLog.d("SDK登陆成功");
                            }

                            @Override
                            public void getDataErrorListener(String s, int i) {

                            }
                        });

                        break;
                    case IRCRequestCode.STATUS_APP_CODE_NO_PHONE_NUMBER:
                        //该用户没有绑定手机号
                        RCToast.Center(context, "没有手机号");
                        break;
                    case IRCRequestCode.STATUS_APP_CODE_PHONE_NUMBER_INVALID:
                        //该用户已经绑定手机号
                        RCToast.Center(context, "该用户已经绑定手机号");
                        break;
                }
            }
        });

        //设置连接测试服务器（公网注释）
        RCSDKManage.setNetworkMethod(true);
        RCSDKManage.setCrash(false);
        RCBaseConfig.setWebViewConfigClassPath(WebViewConfig.class);

        //家庭医生初始化(V2.0版本，使用名医生需要)
        RCFD.init(this);
        //shop配置
        RCShopConfigUtil.setGoodsClassPath(UserChooseConfig.class);

        //扫描工具库初始化
        RCScannerManager.init(this);
        //友盟分享初始化
        RCUmeng.initialize(this, weixinID, weixinSecret, qqID, qqKey);
        //图片库初始化
//        RCBaseConfig.setImageManagerBase(new ImageShowConfig());
        Config.debug = true;
//        com.rocedar.lib.base.config.a.g = true;
//        com.rocedar.lib.base.config.a.b = "http://develop.rocedar.com:8003/";
//        com.rocedar.lib.ba、se.config.a.b = "http://develop.rocedar.com:8003/";
//        com.rocedar.lib.base.config.a.b = "http://192.168.18.146:8080/";
//        Config.P_NETWORK_URL = "http://192.168.18.25/";
//        com.rocedar.lib.base.config.a.b = "http://192.168.18.25/";
    }
}

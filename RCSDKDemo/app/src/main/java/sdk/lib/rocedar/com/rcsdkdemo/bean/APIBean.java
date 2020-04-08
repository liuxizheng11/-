package sdk.lib.rocedar.com.rcsdkdemo.bean;

import com.rocedar.lib.base.network.NetworkMethod;
import com.rocedar.lib.base.network.RCBean;
import com.rocedar.lib.base.userinfo.RCSPUserInfo;

import sdk.lib.rocedar.com.rcsdkdemo.manager.BaseApplication;

/**
 * 项目名称：瑰柏SDK-健康服务（家庭医生）
 * <p>
 * 作者：phj
 * 日期：2018/8/29 上午10:52
 * 版本：V1.1.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class APIBean extends RCBean {

    public APIBean() {
        setP_token("");
        setNetworkMethod(NetworkMethod.API);
        setBaseNetUrl(BaseApplication.APP_NETWORK_URL);
    }

    public String token;

    public String getToken() {
        return RCSPUserInfo.getLastAPIToken();
    }

    public void setToken(String token) {
        this.token = token;
    }
}

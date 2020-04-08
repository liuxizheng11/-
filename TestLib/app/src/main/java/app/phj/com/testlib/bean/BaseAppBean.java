package app.phj.com.testlib.bean;

import com.rocedar.base.network.RCBean;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;

/**
 * 项目名称：设备管理平台测试类
 * <p>
 * 作者：phj
 * 日期：2017/2/15 下午5:05
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class BaseAppBean extends RCBean {

    public String token;

    public String getToken() {
        return RCSPBaseInfo.getLastToken();
    }

}

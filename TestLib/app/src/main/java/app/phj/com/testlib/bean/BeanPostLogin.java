package app.phj.com.testlib.bean;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/14 下午12:11
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class BeanPostLogin extends BaseAppBean {

    public String phone;    //手机号
    public String verification;    //验证码


    public String getPhone() {
        return phone;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}

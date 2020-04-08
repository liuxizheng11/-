package sdk.lib.rocedar.com.rcsdkdemo.bean;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/10/15 下午3:26
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class SaveUserBean extends APIBean {

    public String phone;
    public String cheng_hu;
    public String card_id;
    public String true_name;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCheng_hu() {
        return cheng_hu;
    }

    public void setCheng_hu(String cheng_hu) {
        this.cheng_hu = cheng_hu;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getTrue_name() {
        return true_name;
    }

    public void setTrue_name(String true_name) {
        this.true_name = true_name;
    }
}

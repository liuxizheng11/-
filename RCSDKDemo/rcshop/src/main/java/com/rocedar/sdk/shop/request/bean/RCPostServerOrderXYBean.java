package com.rocedar.sdk.shop.request.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/10/12 下午4:53
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCPostServerOrderXYBean  {



    public String getExtra_param() {
        JSONObject object = new JSONObject();
        try {
            object.put("phone", phone);
            object.put("card_id", card_id);
            object.put("true_name", true_name);
            object.put("relation_user_id", relation_user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }


    private String phone;
    private String card_id;
    private String true_name;
    private String relation_user_id = "-1";

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public void setTrue_name(String true_name) {
        this.true_name = true_name;
    }

    public void setRelation_user_id(String relation_user_id) {
        this.relation_user_id = relation_user_id;
    }


}

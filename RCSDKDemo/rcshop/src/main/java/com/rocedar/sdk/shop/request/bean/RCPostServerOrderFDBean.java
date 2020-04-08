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
public class RCPostServerOrderFDBean {


    public String getExtra_param() {
        JSONObject object = new JSONObject();
        try {
            if (!phone.equals(""))
                object.put("phone", phone);
            object.put("relation_user_id", relation_user_id);
            if (!nick_name.equals(""))
                object.put("nick_name", nick_name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    private String nick_name;
    private String phone;
    private String relation_user_id = "-1";

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRelation_user_id(String relation_user_id) {
        if (!relation_user_id.equals(""))
            this.relation_user_id = relation_user_id;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }
}

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
public class RCPostServerOrderXunYiBean {


    public String getExtra_param() {
        JSONObject object = new JSONObject();
        try {
            if (!advice_id.equals(""))
                object.put("advice_id", advice_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    private String advice_id;


    public void setAdvice_id(String advice_id) {
        if (!advice_id.equals(""))
        this.advice_id = advice_id;
    }
}

package com.rocedar.deviceplatform.request.bean;

/**
 * @author liuyi
 * @date 2017/3/11
 * @desc 家人档案
 * @veison V3.3.30(动吖)
 */

public class BeanGetFamilyRecord extends BasePlatformBean{
    /**
     * 家人用户id
     */
    public String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}

package com.rocedar.sdk.familydoctor.dto.hdp;

/**
 * @author liuyi
 * @date 2018/4/27
 * @desc
 * @veison
 */

public class RCHBPOrganizationListDTO {
    private int org_id;//机构id
    private String org_name;//机构名称
    private String org_icon;//机构图标
    private String android_url;//跳转url
    public int getOrg_id() {
        return org_id;
    }

    public void setOrg_id(int org_id) {
        this.org_id = org_id;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getOrg_icon() {
        return org_icon;
    }

    public void setOrg_icon(String org_icon) {
        this.org_icon = org_icon;
    }

    public String getAndroid_url() {
        return android_url;
    }

    public void setAndroid_url(String android_url) {
        this.android_url = android_url;
    }
}

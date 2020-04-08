package com.rocedar.sdk.familydoctor.request.bean;

import com.rocedar.lib.base.network.RCBean;

/**
 * @author liuyi
 * @date 2017/12/2
 * @desc
 * @veison
 */

public class BeanPostSaveOthersInfo extends RCBean {
    public String sick_name;
    public String sick_sex;
    public String sick_birthday;
    public String sick_height;
    public String sick_weight;

    public String getSick_name() {
        return sick_name;
    }

    public void setSick_name(String sick_name) {
        this.sick_name = sick_name;
    }

    public String getSick_sex() {
        return sick_sex;
    }

    public void setSick_sex(String sick_sex) {
        this.sick_sex = sick_sex;
    }

    public String getSick_birthday() {
        return sick_birthday;
    }

    public void setSick_birthday(String sick_birthday) {
        this.sick_birthday = sick_birthday;
    }

    public String getSick_height() {
        return sick_height;
    }

    public void setSick_height(String sick_height) {
        this.sick_height = sick_height;
    }

    public String getSick_weight() {
        return sick_weight;
    }

    public void setSick_weight(String sick_weight) {
        this.sick_weight = sick_weight;
    }
}

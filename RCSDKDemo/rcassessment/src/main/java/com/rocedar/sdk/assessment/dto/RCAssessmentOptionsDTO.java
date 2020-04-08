package com.rocedar.sdk.assessment.dto;

import java.io.Serializable;

/**
 * 作者：lxz
 * 日期：2018/5/31 下午4:57
 * 版本：V1.0
 * 描述：测评问卷 DTO
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCAssessmentOptionsDTO implements Serializable {
    private String next_topic;
    private int exclusive;
    private int option_id;
    private String option_name;
    private boolean isSelect = false;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getNext_topic() {
        return next_topic;
    }

    public void setNext_topic(String next_topic) {
        this.next_topic = next_topic;
    }

    public int getExclusive() {
        return exclusive;
    }

    public void setExclusive(int exclusive) {
        this.exclusive = exclusive;
    }

    public int getOption_id() {
        return option_id;
    }

    public void setOption_id(int option_id) {
        this.option_id = option_id;
    }

    public String getOption_name() {
        return option_name;
    }

    public void setOption_name(String option_name) {
        this.option_name = option_name;
    }
}

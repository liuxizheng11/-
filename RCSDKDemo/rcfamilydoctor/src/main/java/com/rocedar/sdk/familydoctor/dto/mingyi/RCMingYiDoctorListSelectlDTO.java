package com.rocedar.sdk.familydoctor.dto.mingyi;

import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/7/22 下午1:00
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCMingYiDoctorListSelectlDTO {

    private int id;
    private String name;
    private List<childsDTO> childsDTOS;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<childsDTO> getChildsDTOS() {
        return childsDTOS;
    }

    public void setChildsDTOS(List<childsDTO> childsDTOS) {
        this.childsDTOS = childsDTOS;
    }

    public static class childsDTO {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

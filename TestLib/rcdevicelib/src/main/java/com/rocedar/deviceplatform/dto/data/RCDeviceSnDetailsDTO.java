package com.rocedar.deviceplatform.dto.data;

import java.util.List;

/**
 * @author liuyi
 * @date 2017/2/17
 * @desc SN设备详情DTO
 * @veison
 */

public class RCDeviceSnDetailsDTO {

    /**
     * device_img : /d/m/1204002.png
     * scan : 0
     * config_type : 4
     * roles : [{"role_id":1,"role_img":"/d/m/1220001.png"},{"role_id":2,"role_img":"/d/m/1220001.png"}]
     * relations : [{"device_no":"13249507","relation_id":999,"relation_name":"自己","related_user":114811782959245420,"device_role":1},{"device_no":"13249507","relation_id":1000,"relation_name":"爸爸","related_user":114847954524534270,"device_role":2}]
     */

    /**
     * config_type 配置类型
     * 1 自用单角色,(立方糖护士血糖)
     * 2 自用多角色,(立方乐心血压)
     * 3 家人单角色, (动吖糖护士血糖)
     * 4 家人多角色(动吖乐心血压)
     */
    public static final int SINGLE_SINGLE_ROLE = 1;
    public static final int SINGLE_MORE_ROLE = 2;
    public static final int FAMILY_SINGLE_ROLE = 3;
    public static final int FAMILY_MORE_ROLE = 4;
    /**
     * 设备图片url
     */
    private String device_img;
    /**
     * 是否支持扫描(1 支持，0不支持)
     */
    private int scan;
    /**
     * 配置类型
     * 1 自用单角色,(立方糖护士血糖)
     * 2 自用多角色,(立方乐心血压)
     * 3 家人单角色, (动吖糖护士血糖)
     * 4 家人多角色(动吖乐心血压)
     */
    private int config_type;
    /**
     * Wi-Fi配置url
     */
    private String wifi_url;
    /**
     * 设备角色(按键)
     */
    private List<RolesBean> roles;
    /**
     * 设备已绑定家人信息
     */
    private List<RelationsBean> relations;

    /**
     * 在产品中显示名(设备名称)
     */
    private String display_name;

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }


    public String getDevice_img() {
        return device_img;
    }

    public void setDevice_img(String device_img) {
        this.device_img = device_img;
    }

    public int getScan() {
        return scan;
    }

    public void setScan(int scan) {
        this.scan = scan;
    }

    public int getConfig_type() {
        return config_type;
    }

    public void setConfig_type(int config_type) {
        this.config_type = config_type;
    }

    public List<RolesBean> getRoles() {
        return roles;
    }

    public void setRoles(List<RolesBean> roles) {
        this.roles = roles;
    }

    public List<RelationsBean> getRelations() {
        return relations;
    }

    public void setRelations(List<RelationsBean> relations) {
        this.relations = relations;
    }

    public String getWifi_url() {
        return wifi_url;
    }

    public void setWifi_url(String wifi_url) {
        this.wifi_url = wifi_url;
    }

    public static class RolesBean {
        /**
         * role_id : 1
         * role_img : /d/m/1220001.png
         */
        /**
         * 设备角色(按键)id
         */
        private int role_id;
        /**
         * 设备角色(按键)图片url
         */
        private String role_img;

        public int getRole_id() {
            return role_id;
        }

        public void setRole_id(int role_id) {
            this.role_id = role_id;
        }

        public String getRole_img() {
            return role_img;
        }

        public void setRole_img(String role_img) {
            this.role_img = role_img;
        }
    }

    public static class RelationsBean {
        /**
         * device_no : 13249507
         * relation_id : 999
         * relation_name : 自己
         * related_user : 114811782959245420
         * device_role : 1
         */
        /**
         * 设备sn号
         */
        private String device_no;
        /**
         * 家人关系id
         */
        private int relation_id;
        /**
         * 家人关系名
         */
        private String relation_name;
        /**
         * 家人userId
         */
        private long related_user;
        /**
         * 设备角色(按键)id
         */
        private int device_role_id;
        /**
         * 设备角色(按键)名
         */
        private String device_role_name;

        /**
         * 家人手机号
         */
        private String phoneNumber;

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getDevice_no() {
            return device_no;
        }

        public void setDevice_no(String device_no) {
            this.device_no = device_no;
        }

        public int getRelation_id() {
            return relation_id;
        }

        public void setRelation_id(int relation_id) {
            this.relation_id = relation_id;
        }

        public String getRelation_name() {
            return relation_name;
        }

        public void setRelation_name(String relation_name) {
            this.relation_name = relation_name;
        }

        public long getRelated_user() {
            return related_user;
        }

        public void setRelated_user(long related_user) {
            this.related_user = related_user;
        }

        public int getDevice_role_id() {
            return device_role_id;
        }

        public void setDevice_role_id(int device_role_id) {
            this.device_role_id = device_role_id;
        }

        public String getDevice_role_name() {
            return device_role_name;
        }

        public void setDevice_role_name(String device_role_name) {
            this.device_role_name = device_role_name;
        }
    }
}

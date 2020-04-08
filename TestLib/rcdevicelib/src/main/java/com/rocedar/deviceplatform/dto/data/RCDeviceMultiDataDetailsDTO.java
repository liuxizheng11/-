package com.rocedar.deviceplatform.dto.data;

import java.util.List;

/**
 * @author liuyi
 * @date 2017/3/4
 * @desc 多角色设备详情的DTO
 * @veison V3.3.30(动吖)
 */

public class RCDeviceMultiDataDetailsDTO {

    /**
     * device_name : 鱼跃-37智能血压计
     * users : [{"type_name":"自己","unit":"mmHg","test_time":-1,"device_id":1212002,"user_id":114811782959245420,"icon":"","value":"--","url":"rctp://android##index.CommonIndexActivity##{index_id=4012}"},{"type_name":"爸爸","unit":"mmHg","test_time":-1,"device_id":1212002,"user_id":114847954524534270,"icon":"","value":"--","url":"rctp://android##index.CommonIndexActivity##{user_id=114847954524534265, index_id=4012}"}]
     */

    /**
     * 设备名字
     */
    private String device_name;

    /**
     * 设备行为数据信息
     */
    private List<UsersDTO> users;

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public List<UsersDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UsersDTO> users) {
        this.users = users;
    }

    public static class UsersDTO {
        /**
         * type_name : 自己
         * unit : mmHg
         * test_time : -1
         * device_id : 1212002
         * user_id : 114811782959245420
         * icon :
         * value : --
         * url : rctp://android##index.CommonIndexActivity##{index_id=4012}
         * rights : 0，无权限；1，已授权
         */

        /**
         * 关系称呼
         */
        private String type_name;
        /**
         * 指标单位
         */
        private String unit;
        /**
         * 测试时间
         */
        private long test_time;
        /**
         * 设备id
         */
        private int device_id;
        /**
         * 家人userid
         */
        private long user_id;
        /**
         * A/B键图片
         */
        private String icon;
        /**
         *
         */
        private String value;
        /**
         * 家人详情页面url
         */
        private String url;
        /**
         * 类型id（自己是-1）
         */
        private int type_id;
        /**
         * 0，无权限；1，已授权
         */
        private int rights;

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public long getTest_time() {
            return test_time;
        }

        public void setTest_time(long test_time) {
            this.test_time = test_time;
        }

        public int getDevice_id() {
            return device_id;
        }

        public void setDevice_id(int device_id) {
            this.device_id = device_id;
        }

        public long getUser_id() {
            return user_id;
        }

        public void setUser_id(long user_id) {
            this.user_id = user_id;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getType_id() {
            return type_id;
        }

        public void setType_id(int type_id) {
            this.type_id = type_id;
        }

        public int getRights() {
            return rights;
        }

        public void setRights(int rights) {
            this.rights = rights;
        }
    }
}

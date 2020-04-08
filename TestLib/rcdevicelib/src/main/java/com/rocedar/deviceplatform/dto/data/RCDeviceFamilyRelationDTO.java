package com.rocedar.deviceplatform.dto.data;

/**
 * @author liuyi
 * @date 2017/2/18
 * @desc 查询家人关系列表DTO
 * @veison
 */

public class RCDeviceFamilyRelationDTO {

    /**
     * relation_id : 999
     * relation_name : 爸爸
     * related_user : 114811782959245420
     */

    public RCDeviceFamilyRelationDTO() {
    }

    public RCDeviceFamilyRelationDTO(String relation_name, String phoneNumber) {
        this.relation_name = relation_name;
        this.phoneNumber = phoneNumber;
    }

    /**
     * 家人关系名
     */
    private String relation_name = "";

    private String phoneNumber = "";

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRelation_name() {
        return relation_name;
    }

    public void setRelation_name(String relation_name) {
        this.relation_name = relation_name;
    }

}

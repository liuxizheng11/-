package com.rocedar.deviceplatform.dto.target;

/**
 * 作者：lxz
 * 日期：17/7/7 下午5:21
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCIndicatorTargetWeekDTO {

    /**
     * 目标名称列表
     */
    private String target_names;
    /**
     * 类型名称
     */
    private String target_type_name;
    /**
     * 类型ID
     */
    private int target_type_id;

    //图片URL
    private String imageUrl;

    //建议
    private String suggest;

    //跳转
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getTarget_names() {
        return target_names;
    }

    public void setTarget_names(String target_names) {
        this.target_names = target_names;
    }

    public String getTarget_type_name() {
        return target_type_name;
    }

    public void setTarget_type_name(String target_type_name) {
        this.target_type_name = target_type_name;
    }

    public int getTarget_type_id() {
        return target_type_id;
    }

    public void setTarget_type_id(int target_type_id) {
        this.target_type_id = target_type_id;
    }
}

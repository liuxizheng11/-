package com.rocedar.deviceplatform.dto.target;

/**
 * 作者：lxz
 * 日期：17/7/7 下午5:04
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCIndicatorTargetDayDTO {

    /**
     * 目标ID
     */
    private int target_id;
    /**
     * 目标标题
     */
    private String target_title;
    /**
     * 目标数据
     */
    private float target_data;
    /**
     * 目标值
     */
    private int target_value;
    /**
     * 进度值
     */
    private float progress;
    /**
     * 跳转的URL
     */
    private String url;


    public int getTarget_id() {
        return target_id;
    }

    public void setTarget_id(int target_id) {
        this.target_id = target_id;
    }

    public String getTarget_title() {
        return target_title;
    }

    public void setTarget_title(String target_title) {
        this.target_title = target_title;
    }

    public float getTarget_data() {
        return target_data;
    }

    public void setTarget_data(float target_data) {
        this.target_data = target_data;
    }

    public int getTarget_value() {
        return target_value;
    }

    public void setTarget_value(int target_value) {
        this.target_value = target_value;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

package com.rocedar.deviceplatform.dto.target;

/**
 * 作者：lxz
 * 日期：17/7/7 下午3:56
 * 版本：V2.0.05(健康立方)
 * 描述： 用户目标DTO
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCIndicatorTargetDTO {
    /**
     * 目标ID
     */
    private int target_id;
    /**
     * 目标标题
     */
    private String target_title;
    /**
     * 目标类型ID
     */
    private int target_type_id;
    /**
     * 目标建议
     */
    private String target_suggest;
    /**
     * 目标值
     */
    private int target_value;
    /**
     * 最小值
     */
    private int min;
    /**
     * 最大值
     */
    private int max;
    /**
     * 目标ID
     */
    private String unit;
    /**
     * 状态
     */
    private int status;
    /**
     * 周期ID
     */
    private int period_id;


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

    public int getTarget_type_id() {
        return target_type_id;
    }

    public void setTarget_type_id(int target_type_id) {
        this.target_type_id = target_type_id;
    }

    public String getTarget_suggest() {
        return target_suggest;
    }

    public void setTarget_suggest(String target_suggest) {
        this.target_suggest = target_suggest;
    }

    public int getTarget_value() {
        return target_value;
    }

    public void setTarget_value(int target_value) {
        this.target_value = target_value;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPeriod_id() {
        return period_id;
    }

    public void setPeriod_id(int period_id) {
        this.period_id = period_id;
    }
}

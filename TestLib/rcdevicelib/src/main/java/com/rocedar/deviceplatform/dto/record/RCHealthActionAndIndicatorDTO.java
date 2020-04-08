package com.rocedar.deviceplatform.dto.record;

/**
 * @author liuyi
 * @date 2017/3/7
 * @desc 行为／指标监听
 * @veison V3.3.30(动吖)
 */

public class RCHealthActionAndIndicatorDTO {

    /**
     * conduct_id : 2000
     * indicator_id : -1
     * conduct_name : 步行
     */

    /**
     * 行为ID
     */
    private int conduct_id;
    /**
     * 指标ID
     */
    private int indicator_id;
    /**
     * 行为名称
     */
    private String conduct_name;

    public int getConduct_id() {
        return conduct_id;
    }

    public void setConduct_id(int conduct_id) {
        this.conduct_id = conduct_id;
    }

    public int getIndicator_id() {
        return indicator_id;
    }

    public void setIndicator_id(int indicator_id) {
        this.indicator_id = indicator_id;
    }

    public String getConduct_name() {
        return conduct_name;
    }

    public void setConduct_name(String conduct_name) {
        this.conduct_name = conduct_name;
    }
}

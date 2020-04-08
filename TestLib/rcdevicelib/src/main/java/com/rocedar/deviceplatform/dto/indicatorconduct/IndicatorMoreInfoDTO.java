package com.rocedar.deviceplatform.dto.indicatorconduct;

import java.util.List;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/5/3 下午3:45
 * 版本：V1.0.01
 * 描述：指标数据查看更多（健康立方）
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class IndicatorMoreInfoDTO {


    //开始时间
    private String start_time;
    //结束时间
    private String end_time;
    //单位
    private String unit;
    //测量总次数
    private int total;
    //正常次数
    private int normal;
    //异常次数
    private int unnormal;
    //异常数据
    private List<IndicatorMoreDataDTO> exceptions;
    //近三十天指标数据
    private List<IndicatorMoreDataDTO> history;


    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public int getUnnormal() {
        return unnormal;
    }

    public void setUnnormal(int unnormal) {
        this.unnormal = unnormal;
    }

    public List<IndicatorMoreDataDTO> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<IndicatorMoreDataDTO> exceptions) {
        this.exceptions = exceptions;
    }

    public List<IndicatorMoreDataDTO> getHistory() {
        return history;
    }

    public void setHistory(List<IndicatorMoreDataDTO> history) {
        this.history = history;
    }
}

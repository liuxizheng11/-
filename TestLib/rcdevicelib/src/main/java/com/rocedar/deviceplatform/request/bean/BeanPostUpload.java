package com.rocedar.deviceplatform.request.bean;

/**
 * @author liuyi
 * @date 2017/2/17
 * @desc 行为数据上传
 * @veison
 */

public class BeanPostUpload extends BasePlatformBean {
    /**
     * 上传格式（需要将jsonarray转换为string）
     * [{ "device_id": <设备ID，int>,
     * "conduct_id": <行为ID（无对应行为ID时传-1），int>,
     * "date": <数据时间（精确到秒），long>,
     * "value": { <指标ID，int>:<指标值（处理：单位、精度），String> }}]
     * <p>
     * 上传数据说明:
     * [{
     * "device_id": 1000000,
     * "conduct_id": 2000,
     * "date": 20170117154000,
     * "value": {
     * "4000": 10000, 步行步数
     * "4026": 5.32, 步行距离(千米两位小数)
     * "4027": 35, 步行卡路里(千卡两位小数)
     * "4037": 30  步行时长(分钟向上取整) } },
     * {
     * "device_id": 1000000,
     * "conduct_id": 2001,
     * "date": 20170117154000,
     * "value": {
     * "4001": 5.32, 跑步距离(千米两位小数)
     * "4024": 32, 跑步时长(分钟向上取整)
     * "4025": 42.34, 跑步卡路里(千卡两位小数)
     * "4036": 1231 跑步步数 }},
     * {
     * "device_id": 1201001,
     * "conduct_id": 2002,
     * "date": 20170117134000,
     * "value": {
     * "4009": 20170116221000, 入睡时间yyyyMMddHHmmss
     * "4028": 20170117083000, 醒来时间yyyyMMddHHmmss
     * "4029": 600, 睡眠时长分钟
     * "4030": 300, 深睡时长分钟
     * "4031": 200, 浅睡时长分钟
     * "4032": 100, 清醒时长分钟
     * "4033": 5 清醒次数} },
     * {
     * "device_id": 1201001,
     * "conduct_id": -1,
     * "date": 20170117205500,
     * "value": {
     * "4013": 48, 心率}},
     * {
     * "device_id": 1201001,
     * "conduct_id": -1,
     * "date": 20170117105500,
     * "value": {
     * "4012": 160;80 血压(高压;低压)
     * "4013": 98 心率}}]
     */
    public String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

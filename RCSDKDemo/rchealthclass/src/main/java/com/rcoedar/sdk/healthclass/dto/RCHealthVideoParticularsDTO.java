package com.rcoedar.sdk.healthclass.dto;

/**
 * 作者：lxz
 * 日期：2018/9/20 下午5:52
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCHealthVideoParticularsDTO {
    //图片
    private String thumbnail;
    //视频地址
    private String info_url;
    //阅读人数
    private String read_num;
    //标题
    private String title;
    //详情描述
    private String detail_desc;
    //医院信息
    private String hospital;
    //医生名称
    private String doctor_name;
    //医生职位
    private String title_name;
    //分享地址
    private String shareUrl;
    //视频大小(单位M)
    private float video_size;
    //视频时长（单位s
    private int video_time;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getInfo_url() {
        return info_url;
    }

    public void setInfo_url(String info_url) {
        this.info_url = info_url;
    }

    public String getRead_num() {
        return read_num;
    }

    public void setRead_num(String read_num) {
        this.read_num = read_num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail_desc() {
        return detail_desc;
    }

    public void setDetail_desc(String detail_desc) {
        this.detail_desc = detail_desc;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public float getVideo_size() {
        return video_size;
    }

    public void setVideo_size(float video_size) {
        this.video_size = video_size;
    }

    public int getVideo_time() {
        return video_time;
    }

    public void setVideo_time(int video_time) {
        this.video_time = video_time;
    }
}

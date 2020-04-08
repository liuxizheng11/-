package com.rcoedar.sdk.healthclass.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @desc
 * @veison
 */

public class RCHealthClassroomDTO {

    /**
     * infoId : 100056
     * title : dgdf
     * thumbnail :
     * infoURL : http://192.168.18.21/manage/health/info/detail/?infoId=100056&t=1499396275584
     * readNum : 1.3589万
     */
//	"recommend": [{
//        "video_time": 181,
//                "thumbnail": "/h/i/ad12/head.jpg",
//                "read_num": "303人",
//                "video_size": 13.4,
//                "subject_name_list": ["血压"],
//        "info_id": 100301,
//                "video": 1,
//                "title": "高血压患者什么时间吃药好",
//                "info_url": "http://develop.rocedar.com:8005/p/health/info/video/100301/?from=SDK&title=高血压患者什么时间吃药好&desc=01-北京中医药大学东方医院-心血管内科-副主任医师-李岩-高血压患者什么时间吃药好"
//    },
    private int video_time;
    private int info_id;
    private double video_size;
    private String title;
    private String thumbnail;
    private String info_url;
    private String read_num;
    private List<String> subject_name_list;
    private int video;


    public int getVideo_time() {
        return video_time;
    }

    public void setVideo_time(int video_time) {
        this.video_time = video_time;
    }

    public double getVideo_size() {
        return video_size;
    }

    public void setVideo_size(double video_size) {
        this.video_size = video_size;
    }

    public int getInfo_id() {
        return info_id;
    }

    public void setInfo_id(int info_id) {
        this.info_id = info_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public List<String> getSubject_name_list() {
        return subject_name_list;
    }

    public void setSubject_name_list(List<String> subject_name_list) {
        this.subject_name_list = subject_name_list;
    }

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }
}

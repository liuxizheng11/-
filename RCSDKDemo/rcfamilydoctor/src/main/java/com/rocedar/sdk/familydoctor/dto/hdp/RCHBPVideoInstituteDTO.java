package com.rocedar.sdk.familydoctor.dto.hdp;


import com.rocedar.lib.base.manage.RCBaseGsonDTO;

import java.util.List;

/**
 * @author liuyi
 * @date 2017/11/27
 * @desc
 * @veison
 */

public class RCHBPVideoInstituteDTO extends RCBaseGsonDTO {

    /**
     * title : 怎么调整高血压
     * video_url : /img/org/
     * cathedra_id : 10000
     * video_img : /img/org/
     * video_time : 03:32
     * label : 美容
     * access_count : 1001人阅读
     */
    /**
     * 视频主题
     */
    private String title;
    /**
     * 视频地址
     */
    private String video_url;
    /**
     * 视频id
     */
    private int cathedra_id;
    /**
     * 视频缩略图
     */
    private String video_img;
    /**
     * 视频时间
     */
    private String video_time;
    /**
     * 视频标签
     */
    private List<String> label;
    /**
     * 观看量
     */
    private String access_count;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public int getCathedra_id() {
        return cathedra_id;
    }

    public void setCathedra_id(int cathedra_id) {
        this.cathedra_id = cathedra_id;
    }

    public String getVideo_img() {
        return video_img;
    }

    public void setVideo_img(String video_img) {
        this.video_img = video_img;
    }

    public String getVideo_time() {
        return video_time;
    }

    public void setVideo_time(String video_time) {
        this.video_time = video_time;
    }

    public List<String> getLabel() {
        return label;
    }

    public void setLabel(List<String> label) {
        this.label = label;
    }

    public String getAccess_count() {
        return access_count;
    }

    public void setAccess_count(String access_count) {
        this.access_count = access_count;
    }
}

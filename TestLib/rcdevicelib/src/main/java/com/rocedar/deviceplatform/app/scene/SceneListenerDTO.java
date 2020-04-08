package com.rocedar.deviceplatform.app.scene;

/**
 * 项目名称：FangZhou2.1
 * <p>
 * 作者：phj
 * 日期：2017/8/3 下午3:08
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class SceneListenerDTO {

    //速度（km/h）
    public double speed;
    //持续总时间（s）
    public long allTime;
    //持续总时间（h:m:s）
    public String showTime;
    //总距离
    public double allDistance;
    //卡路里（cal）
    public double calorie;
    //
    public int pace;
    //
    public String paceShow="--'";

    public int getPace() {
        return pace;
    }

    public String getPaceShow() {
        return paceShow;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
        if (speed > 0) {
            this.pace = (int) (3600 / speed);
            this.paceShow = pace / 60 + "'" + pace % 60 + "\"";
        } else {
            this.pace = 0;
            this.paceShow = "--'";
        }
    }

    public long getAllTime() {
        return allTime;
    }

    public void setAllTime(long allTime) {
        this.allTime = allTime;
    }

    public double getAllDistance() {
        return allDistance;
    }

    public void setAllDistance(double allDistance) {
        this.allDistance = allDistance;
    }

    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }
}

package com.rocedar.deviceplatform.app.scene;

/**
 * 项目名称：FangZhou2.1
 * <p>
 * 作者：phj
 * 日期：2017/8/9 上午10:28
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class SceneSpeedDTO {

    //时间 min
    private String time;
    //速度 km/h
    private double speed;
    //配速 h/km
    private double pace;
    //距离 km
    private double distance;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
        if (speed > 0)
            this.pace = 1 / speed;
        else
            this.speed = 0;
    }

    public double getPace() {
        return pace;
    }


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}

package com.rocedar.deviceplatform.app.scene;

import java.util.List;

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

public class SceneGPSListDTO {

    //跑步开始时间
    public String startTime;

    //GPS列表
    public List<SceneGPSDTO> sceneGPSDTOs;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public List<SceneGPSDTO> getSceneGPSDTOs() {
        return sceneGPSDTOs;
    }

    public void setSceneGPSDTOs(List<SceneGPSDTO> sceneGPSDTOs) {
        this.sceneGPSDTOs = sceneGPSDTOs;
    }
}

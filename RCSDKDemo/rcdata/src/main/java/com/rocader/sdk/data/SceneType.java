package com.rocader.sdk.data;

/**
 * 项目名称：TestLib
 * <p>
 * 作者：phj
 * 日期：2017/7/18 下午4:58
 * 版本：V1.0.06
 * 描述：运动类型
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public enum SceneType {


    //室内跑步，室外跑，室内骑行，室外骑行
    RUN, RUNGPS, CYCLING, CYCLINGGPS, NONE;

    public static String getString(SceneType sceneType) {
        switch (sceneType) {
            case RUN:
                return "室内跑步";
            case RUNGPS:
                return "室外跑步";
            case CYCLING:
                return "室内骑行";
            case CYCLINGGPS:
                return "室外骑行";
        }
        return "";
    }

}

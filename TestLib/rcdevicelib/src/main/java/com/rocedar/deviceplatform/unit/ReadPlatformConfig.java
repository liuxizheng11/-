package com.rocedar.deviceplatform.unit;

import com.rocedar.base.RCJavaUtil;
import com.rocedar.base.RCLog;

import org.json.JSONException;
import org.json.JSONObject;

import static com.rocedar.deviceplatform.unit.AESUtils.TAG;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/8/31 下午6:59
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class ReadPlatformConfig {

    private static final String CONFIG = "platformConfig";


    private static JSONObject getCircleAllInfo() {
        try {
            return new JSONObject(RCJavaUtil.getFromAssets(CONFIG));
        } catch (JSONException e) {
            RCLog.e("－－－找不到任务数据");
            return new JSONObject();
        }
    }

    private static final String KEY_FAMILYDOCTOR = "family_doctor";

    private static final String KEY_FAMILYDOCTOR_INTRODUCED = "family_doctor_introduced";

    /**
     * 家庭医生实现类
     *
     * @return
     */
    public static String getFamilyDoctorClass() {
        String className = getCircleAllInfo().optString(KEY_FAMILYDOCTOR);
        return className != null ? className : "";
    }

    /**
     * 家庭医生评价有关实现类
     *
     * @return
     */
    public static Class getFamilyDoctorIntroducedClass() {
        String className = getCircleAllInfo().optString(KEY_FAMILYDOCTOR_INTRODUCED);
        if (className != null && !className.equals("")) {
            try {
                return Class.forName(className);
            } catch (Exception e) {
                e.printStackTrace();
                RCLog.e(TAG, "类对象获取失败");
            }
        }
        return null;
    }


}

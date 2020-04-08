package com.rocedar.deviceplatform.dto.target;

/**
 * 项目名称：FangZhou2.1
 * <p>
 * 作者：phj
 * 日期：2017/7/12 下午3:40
 * 版本：V2.2.00
 * 描述：(1.1.4 获取目标类型)
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCIndicatorTargetWeekTypeDTO {


    public int target_type_id;        //int	类型ID
    public String target_type_img;    //	String	背景图
    public String target_type_name;    //	String	类型名称


    public int getTarget_type_id() {
        return target_type_id;
    }

    public void setTarget_type_id(int target_type_id) {
        this.target_type_id = target_type_id;
    }

    public String getTarget_type_img() {
        return target_type_img;
    }

    public void setTarget_type_img(String target_type_img) {
        this.target_type_img = target_type_img;
    }

    public String getTarget_type_name() {
        return target_type_name;
    }

    public void setTarget_type_name(String target_type_name) {
        this.target_type_name = target_type_name;
    }
}

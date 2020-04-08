package com.rocedar.deviceplatform.dto.familydoctor;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/4/20 上午11:20
 * 版本：V1.0.01
 * 描述：医生部门列表
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCFDDepartmentDTO {

    //int	科室ID，微问诊系统内唯一标示
    public int department_id;
    //String	科室名字
    public String department_name;

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }
}

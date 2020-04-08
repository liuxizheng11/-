package com.rocedar.sdk.familydoctor.dto;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/22 上午10:19
 * 版本：V1.0.00
 * 描述：瑰柏SDK-科室列表数据对象
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

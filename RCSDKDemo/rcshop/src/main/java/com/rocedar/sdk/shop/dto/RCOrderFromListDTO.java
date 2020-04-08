package com.rocedar.sdk.shop.dto;

/**
 * 作者：lxz
 * 日期：2018/7/13 下午3:18
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCOrderFromListDTO {
    //       fee		Float	待付金额
//    fee_paid		Float	实付金额
//    service_time		Long	确认服务时间
//    payment_type		Int	付款类型(1000:微信;1001:支付宝)
//    create_time		Long	订单生成时间
//    expire_time		Long	支付失效时间
//    reservation_time		Long	期望服务时间
//    patient_id		Long	病人ID
//    order_id		Int	订单id
//    order_type		Int	订单类型
//    snapshot		Dict	医生信息快照
//    doctor_name	String	医生名称
//    title_name	String	医生职称
//    hospital_name	String	医院名称
//    profession_name	String	科室
//    service_type_name	String	服务类型名称
//    pay_time	Long	支付时间
//    status	Int	订单状态(-1: 删除；0: 已取消；1: 待付款；2：已支付，3：已完成；4: 已退款
//    int server_status 开通状态 = 0 未开通; = 1 已开通
//    int server_user_type  服务类型 = 0 他人; = 1 自用
    private float fee_paid;
    private float fee;
    private long service_time;
    private long create_time;
    private String order_name;
    private String order_icon;
    private long expire_time;
    private long reservation_time;
    private long patient_id;
    private int order_id;
    private int order_type;
    private String doctor_name;
    private String title_name;
    private String hospital_name;
    private String profession_name;
    private String service_type_name;
    private String server_time;
    private long pay_time;
    private int status;
    private int service_type_id;
    private int server_status;
    private int server_user_type;

    public String getServer_time() {
        return server_time;
    }

    public void setServer_time(String server_time) {
        this.server_time = server_time;
    }

    public int getServer_status() {
        return server_status;
    }

    public void setServer_status(int server_status) {
        this.server_status = server_status;
    }

    public int getServer_user_type() {
        return server_user_type;
    }

    public void setServer_user_type(int server_user_type) {
        this.server_user_type = server_user_type;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getOrder_icon() {
        return order_icon;
    }

    public void setOrder_icon(String order_icon) {
        this.order_icon = order_icon;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public int getService_type_id() {
        return service_type_id;
    }

    public void setService_type_id(int service_type_id) {
        this.service_type_id = service_type_id;
    }

    public float getFee_paid() {
        return fee_paid;
    }

    public void setFee_paid(float fee_paid) {
        this.fee_paid = fee_paid;
    }

    public long getService_time() {
        return service_time;
    }

    public void setService_time(long service_time) {
        this.service_time = service_time;
    }


    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(long expire_time) {
        this.expire_time = expire_time;
    }

    public long getReservation_time() {
        return reservation_time;
    }

    public void setReservation_time(long reservation_time) {
        this.reservation_time = reservation_time;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getOrder_type() {
        return order_type;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getProfession_name() {
        return profession_name;
    }

    public void setProfession_name(String profession_name) {
        this.profession_name = profession_name;
    }

    public String getService_type_name() {
        return service_type_name;
    }

    public void setService_type_name(String service_type_name) {
        this.service_type_name = service_type_name;
    }

    public long getPay_time() {
        return pay_time;
    }

    public void setPay_time(long pay_time) {
        this.pay_time = pay_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(long patient_id) {
        this.patient_id = patient_id;
    }
}

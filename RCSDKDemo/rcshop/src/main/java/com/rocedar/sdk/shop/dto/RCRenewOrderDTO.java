package com.rocedar.sdk.shop.dto;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/10/15 下午5:48
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCRenewOrderDTO {


    private int skuId;
    private int goodsId;
    private int outerId;
    private String validityPeriodName = "";
    private String feeName = "";
    private String skuName = "";
    private String server_time = "";
    //商品类型ID
    private int typeId = 1002;

    private String trueName = "";
    private String idCardNo = "";
    private long phoneNo;
    //商品购买说明
    private String purchaseNotes = "";
    private long relation_user_id;

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getOuterId() {
        return outerId;
    }

    public void setOuterId(int outerId) {
        this.outerId = outerId;
    }


    public String getValidityPeriodName() {
        return validityPeriodName;
    }

    public void setValidityPeriodName(String validityPeriodName) {
        this.validityPeriodName = validityPeriodName;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getServer_time() {
        return server_time;
    }

    public void setServer_time(String server_time) {
        this.server_time = server_time;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public long getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(long phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPurchaseNotes() {
        return purchaseNotes;
    }

    public void setPurchaseNotes(String purchaseNotes) {
        this.purchaseNotes = purchaseNotes;
    }

    public long getRelation_user_id() {
        return relation_user_id;
    }

    public void setRelation_user_id(long relation_user_id) {
        this.relation_user_id = relation_user_id;
    }
}

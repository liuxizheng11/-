package com.rocedar.sdk.shop.dto;

/**
 * 项目名称：瑰柏SDK-健康服务（家庭医生）
 * <p>
 * 作者：phj
 * 日期：2018/9/12 下午5:15
 * 版本：V1.1.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCServerGoodsInfoDTO {

    private int skuId;
    private int goodsId;
    private int outerId;
    private String skuName = "";
    private String skuBanner = "";
    private String skuTitle = "";
    private String skuSubTitle = "";
    private int validityPeriod;
    private String validityPeriodName = "";
    private float fee;
    private String feeName = "";
    private String skuDesc = "";
    private String sku_icon;
    private int status;
    //是否允许他人使用
    private boolean selfUse;
    //商品购买说明
    private String purchaseNotes;
    //商品类型ID
    private int typeId = 1002;
    //是否被选中
    private boolean isSelect = false;

    public String getSku_icon() {
        return sku_icon;
    }

    public void setSku_icon(String sku_icon) {
        this.sku_icon = sku_icon;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

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

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuBanner() {
        return skuBanner;
    }

    public void setSkuBanner(String skuBanner) {
        this.skuBanner = skuBanner;
    }

    public String getSkuTitle() {
        return skuTitle;
    }

    public void setSkuTitle(String skuTitle) {
        this.skuTitle = skuTitle;
    }

    public String getSkuSubTitle() {
        return skuSubTitle;
    }

    public void setSkuSubTitle(String skuSubTitle) {
        this.skuSubTitle = skuSubTitle;
    }

    public int getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(int validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public String getValidityPeriodName() {
        return validityPeriodName;
    }

    public void setValidityPeriodName(String validityPeriodName) {
        this.validityPeriodName = validityPeriodName;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getSkuDesc() {
        return skuDesc;
    }

    public void setSkuDesc(String skuDesc) {
        this.skuDesc = skuDesc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSelfUse() {
        return selfUse;
    }

    public void setSelfUse(boolean selfUse) {
        this.selfUse = selfUse;
    }

    public String getPurchaseNotes() {
        return purchaseNotes;
    }

    public void setPurchaseNotes(String purchaseNotes) {
        this.purchaseNotes = purchaseNotes;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}

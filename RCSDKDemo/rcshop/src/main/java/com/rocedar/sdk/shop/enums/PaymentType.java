package com.rocedar.sdk.shop.enums;

/**
 * 项目名称：瑰柏SDK-健康服务（家庭医生）
 * <p>
 * 作者：phj
 * 日期：2018/7/30 下午3:59
 * 版本：V1.1.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public enum PaymentType {

    WEIXIN(1000), ALIPAY(1001);

    PaymentType(int typeId) {
        this.typeId = typeId;
    }

    public int typeId;

    public int getTypeId() {
        return typeId;
    }
}

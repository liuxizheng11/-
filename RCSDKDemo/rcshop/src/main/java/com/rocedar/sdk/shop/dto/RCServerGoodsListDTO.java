package com.rocedar.sdk.shop.dto;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/9/20 下午5:46
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品列表数据模型
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCServerGoodsListDTO {

    private int goods_id;
    private String goods_Name = "";
    private String goods_desc = "";

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_Name() {
        return goods_Name;
    }

    public void setGoods_Name(String goods_Name) {
        this.goods_Name = goods_Name;
    }

    public String getGoods_desc() {
        return goods_desc;
    }

    public void setGoods_desc(String goods_desc) {
        this.goods_desc = goods_desc;
    }
}

package com.rocedar.sdk.shop.config;

import android.content.Context;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/9/27 下午2:30
 * 版本：V1.1.00
 * 描述：瑰柏SDK-商品配置项
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface IRCGoodsConfig {

    /**
     * 获取用户（家人列表）
     *
     * @return
     */
    boolean getChooseUserList(Context context, IRCShopGoodsChooseUserListener listener);


    boolean saveUserInfo(Context context, String nickName, String phoneNo, String idCardNo,
                         String trueName, IRCShopGoodsPostUserListener listener);


}

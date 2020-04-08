package com.rocedar.sdk.familydoctor;

import android.app.Application;

import com.rocedar.sdk.familydoctor.config.ShopConfigImpl;
import com.rocedar.sdk.familydoctor.config.YunXinUtil;
import com.rocedar.sdk.shop.config.RCShopConfigUtil;

/**
 * 项目名称：瑰柏SDK-健康服务（家庭医生）
 * <p>
 * 作者：phj
 * 日期：2018/8/9 下午6:39
 * 版本：V1.1.00
 * 描述：瑰柏SDK-家庭医生初始化
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFD {

    public static void init(Application application) {
        //设置shop的配置
        RCShopConfigUtil.setClassPath(ShopConfigImpl.class);

        //初始化云信
        if (YunXinUtil.hasYunXin()) {
            YunXinUtil.initYunXin(application);
        }
    }

}

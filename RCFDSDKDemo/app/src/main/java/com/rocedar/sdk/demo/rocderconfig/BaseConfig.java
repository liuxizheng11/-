package com.rocedar.sdk.demo.rocderconfig;

import com.rocedar.lib.base.config.IRCBaseUtil;

/**
 * 项目名称：瑰柏SDK-DEMO
 * <p>
 * 作者：phj
 * 版本：V1.0.00
 * 描述：瑰柏SDK-基础图片配置，不修改传-1
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class BaseConfig implements IRCBaseUtil {

    /**
     * @return 设置加载状态的loading图片资源
     */
    @Override
    public int imageResLoading() {
        return -1;
    }

    /**
     * @return 设置默认图片
     */
    @Override
    public int imageResDefaultHead() {
        return -1;
    }

    /**
     * @return 头像默认图片
     */
    @Override
    public int imageResDefault() {
        return -1;
    }
}

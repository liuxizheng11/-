package com.rocedar.lib.base.image.load;

import android.content.Context;

/**
 * 项目名称：瑰柏SDK-健康服务（家庭医生）
 * <p>
 * 作者：phj
 * 日期：2018/8/25 下午3:57
 * 版本：V1.1.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface IRCImageManager extends IRCImageManagerBase {

    void setHoldImage(int imageRes);

    void setErrorImage(int imageRes);

    long getImageDiskCacheSize(Context context);

    /**
     * 清除缓存
     *
     * @param context
     */
    void clearImageDiskCache(Context context);

}

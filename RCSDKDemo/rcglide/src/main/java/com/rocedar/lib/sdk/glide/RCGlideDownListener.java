package com.rocedar.lib.sdk.glide;

import java.io.File;

/**
 * 项目名称：瑰柏SDK-健康服务（家庭医生）
 * <p>
 * 作者：phj
 * 日期：2018/8/30 下午9:01
 * 版本：V1.1.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCGlideDownListener {

    void onLoadFailed(String info);

    void onResourceReady(File resource);

}

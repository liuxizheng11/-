package com.rocedar.lib.base.config;

import android.app.Activity;

import com.rocedar.lib.base.R;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/25 下午4:57
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCBaseUtilImpl implements IRCBaseUtil {

    @Override
    public int imageResLoading() {
        return R.mipmap.rc_handler;
    }

    @Override
    public int imageResDefaultHead() {
        return R.mipmap.rc_head_default;
    }

    @Override
    public int imageResDefault() {
        return R.mipmap.rc_image_default;
    }

    @Override
    public boolean share() {
        return false;
    }

    @Override
    public void share(Activity context, String image, String title, String msg, String url) {

    }

}

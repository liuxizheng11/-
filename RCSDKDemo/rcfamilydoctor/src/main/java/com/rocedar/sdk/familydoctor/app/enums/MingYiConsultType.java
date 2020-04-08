package com.rocedar.sdk.familydoctor.app.enums;

import android.content.Context;

import com.rocedar.sdk.familydoctor.R;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/7/23 下午4:57
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public enum MingYiConsultType {

    TEXT(R.string.rc_mingyi_consult_text, 1000),
    PHONE(R.string.rc_mingyi_consult_phone, 1001),
    VIDEO(R.string.rc_mingyi_consult_video, 1002);


    private int showStringID;
    private int typeId;


    MingYiConsultType(int showStringId, int typeId) {
        this.showStringID = showStringId;
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getShowString(Context context) {
        return context.getString(showStringID);
    }
}

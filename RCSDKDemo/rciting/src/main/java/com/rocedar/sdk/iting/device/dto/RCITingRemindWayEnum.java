package com.rocedar.sdk.iting.device.dto;

import com.rocedar.sdk.iting.R;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2019/4/23 2:57 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public enum RCITingRemindWayEnum {

    Sound(0, R.string.rc_iting_sound, 0),
    Vibrations(0, R.string.rc_iting_vibrations, 1),
    VibrationsDouble(0, R.string.rc_iting_vibrations_double, 3),
    SoundDouble(0, R.string.rc_iting_sound_double, 4),
    VibrationsAndSound(0, R.string.rc_iting_vibrations, 2);


    RCITingRemindWayEnum(int typeId, int typeName, int indexId) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.indexId = indexId;
    }

    private int typeId;
    private int typeName;
    private int indexId;

    public int getTypeId() {
        return typeId;
    }

    public int getTypeName() {
        return typeName;
    }

    public int getIndexId() {
        return indexId;
    }
}

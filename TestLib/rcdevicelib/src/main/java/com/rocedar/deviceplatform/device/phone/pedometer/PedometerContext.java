package com.rocedar.deviceplatform.device.phone.pedometer;

import android.content.Context;

import com.rocedar.base.RCBaseManage;

/**
 * SDK的全局
 */
public abstract class PedometerContext {

    private static PedometerManager manager;

    private static void initContext(Context context) {
        manager = new PedometerManager(context);
    }

    public static PedometerManager getManager() {
        if (manager == null)
            initContext(RCBaseManage.getInstance().getContext());
        return manager;
    }
}

package com.rocedar.deviceplatform.device.phone.listener;

/**
 * 回调接口
 */
public abstract class InterfaceCallBack {

    /*步数回调*/
    public interface StepNumCallback {
        void onStepNumCallbackAcc(int num);

        void onStepNumCallbackCou(int num, long time);

    }

    /*踩下脚步时回调*/
    public interface StepDetectorCallback {
        void onStepDetectorCallback();
    }



}

package com.rocedar.lib.base.unit;

import android.content.Context;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.view.View;

import com.rocedar.lib.base.permission.Acp;
import com.rocedar.lib.base.permission.AcpListener;
import com.rocedar.lib.base.permission.AcpOptions;
import com.rocedar.lib.base.permission.GoPermissionUtil;

import java.util.List;

/**
 * 项目名称：TestLib
 * <p>
 * 作者：phj
 * 日期：2017/8/17 下午2:16
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCPermissionUtil {

    private static RCDialog dialog;


    private static void showDialog(final Context context, String info) {
        dialog = new RCDialog(context, new String[]{null, info, "取消", "去设置"}, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                RCToast.Center(context, "请设置权限后重试");
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                GoPermissionUtil.gotoPermission(context);
            }
        });
        dialog.show();
    }

    private static void showDialog6(final Context context, String info) {
        dialog = new RCDialog(context, new String[]{null, info, "取消", "去设置"}, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                RCToast.Center(context, "请设置权限后重试");
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                GoPermissionUtil.gotoPermission6(context);
            }
        });
        dialog.show();
    }

    private final static String PREMISSION_CAMERA_BASE = "请检查是否已经打开权限";
    private final static String PREMISSION_CAMERA_NONE = "请检查是否已经打开摄像头权限";
    private final static String PREMISSION_AUDIO_NONE = "请检查是否已经打开麦克风(本地录音)权限";


    /**
     * 获取对应权限
     *
     * @param mContext
     * @param acpListener
     * @param permission
     */
    public static void getPremission(final Context mContext, final AcpListener acpListener, final String... permission) {
        if (permission.length <= 0) return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            for (int i = 0; i < permission.length; i++) {
                if (!checkIsOpen(mContext, permission[i])) {
                    showDialog(mContext, getPremissionInfo(permission[i]));
                    return;
                }
            }
            acpListener.onGranted();
        } else {
            Acp.getInstance(mContext).request(new AcpOptions.Builder()
                    .setPermissions(permission).build(), new AcpListener() {
                @Override
                public void onGranted() {
                    for (int i = 0; i < permission.length; i++) {
                        if (!checkIsOpen(mContext, permission[i])) {
                            showDialog6(mContext, getPremissionInfo(permission[i]));
                            return;
                        }
                    }
                    acpListener.onGranted();
                }

                @Override
                public void onDenied(List<String> permissions) {
                    acpListener.onDenied(permissions);
                }

            });
        }
    }


    private static boolean checkIsOpen(Context context, String permissionName) {
        if (permissionName.equals(android.Manifest.permission.CAMERA)) {
            return cameraIsCanUse();
        } else if (permissionName.equals(android.Manifest.permission.RECORD_AUDIO)) {
            return isHasPermission(context);
        }
        return true;
    }

    private static String getPremissionInfo(String permissionName) {
        if (permissionName.equals(android.Manifest.permission.CAMERA)) {
            return PREMISSION_CAMERA_NONE;
        } else if (permissionName.equals(android.Manifest.permission.RECORD_AUDIO)) {
            return PREMISSION_AUDIO_NONE;
        }
        return PREMISSION_CAMERA_BASE;
    }

    /**
     * 检测是否有拍照权限（适用于6.0以下）
     *
     * @return
     */
    public static boolean cameraIsCanUse() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }
        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }


    // 音频获取源
    public static int audioSource = MediaRecorder.AudioSource.MIC;
    // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    public static int sampleRateInHz = 44100;
    // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
    public static int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
    // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
    public static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    // 缓冲区字节大小
    public static int bufferSizeInBytes = 0;

    /**
     * 判断是是否有录音权限
     */
    public static boolean isHasPermission(final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return true;
        }
        AudioRecord audioRecord = null;
        try {
            bufferSizeInBytes = 0;
            bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,
                    channelConfig, audioFormat);
            audioRecord = new AudioRecord(audioSource, sampleRateInHz,
                    channelConfig, audioFormat, bufferSizeInBytes);
            //开始录制音频
            // 防止某些手机崩溃，例如联想
            audioRecord.startRecording();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * 根据开始录音判断是否有录音权限
         */
        if (audioRecord == null || audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
            return false;
        }
        audioRecord.stop();
        audioRecord.release();
        audioRecord = null;
        return true;
    }

}

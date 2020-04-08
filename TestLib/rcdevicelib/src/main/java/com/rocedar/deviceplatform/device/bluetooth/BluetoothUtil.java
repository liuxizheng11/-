package com.rocedar.deviceplatform.device.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import com.rocedar.base.RCDialog;
import com.rocedar.deviceplatform.R;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/9 下午7:28
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public abstract class BluetoothUtil {


    public static boolean checkBluetooth(Context mContext) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return false;
        }
        // 检查当前手机是否支持ble 蓝牙,如果不支持退出程序
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(mContext, R.string.rcdevice_ble_not_supported, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean checkBluetoothIsOpen() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        // 如果本地蓝牙没有开启，则开启
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            return true;
        }
        return false;
    }

    private static RCDialog rcDialog;

    public static boolean isOpenFirst = true;

    public static void openBluetoothDialog(final Activity mContext) {
        openBluetoothDialog(mContext, -1);
    }

    public static void openBluetoothDialog(final Activity mContext, final int result) {
        if (!isOpenFirst) {
            return;
        }
        isOpenFirst = false;
        rcDialog = new RCDialog(mContext, new String[]{
                null, "您有蓝牙设备需要连接，是否开启蓝牙？", "", ""
        }, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                if (result > 0) {
                    mContext.startActivityForResult(mIntent, 1);
                } else
                    mContext.startActivity(mIntent);
                rcDialog.dismiss();
            }
        });
        if (!rcDialog.isShowing())
            rcDialog.show();
    }

    public static void connectErrorDialog(final Activity mContext, final View.OnClickListener onClickListener) {
        rcDialog = new RCDialog(mContext, new String[]{
                "设备连接失败", "请确认设备是否在附近；如果多次失败请尝试重启蓝牙后重试。", "取消", "重试"
        }, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rcDialog.dismiss();
                onClickListener.onClick(v);
            }
        });
        if (!rcDialog.isShowing())
            rcDialog.show();
    }
}
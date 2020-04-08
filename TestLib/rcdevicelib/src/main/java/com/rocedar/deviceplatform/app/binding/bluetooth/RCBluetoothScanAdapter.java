package com.rocedar.deviceplatform.app.binding.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.sharedpreferences.RCSPDeviceInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 蓝牙配对列表
 *
 * @author lxz
 */
public class RCBluetoothScanAdapter extends BaseAdapter {

    private ArrayList<BluetoothDevice> mLeDevices = new ArrayList<>();
    private ArrayList<String> mDistances = new ArrayList<>();
    private LayoutInflater mInflater;
    private RCBluetoothScanActivity mActivity;

    private int device_id;

    public RCBluetoothScanAdapter(RCBluetoothScanActivity activity, int device_id) {
        this.mActivity = activity;
        this.mInflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.device_id = device_id;
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mLeDevices.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    public void clear() {
//        mLeDevices.clear();
//    }


    public void addDevice(BluetoothDevice device, double pow) {
        if (!mLeDevices.contains(device)) {
            mLeDevices.add(device);
            DecimalFormat df = new DecimalFormat("#.##");
            String st = df.format(pow);
            mDistances.add(st + "米内");
        }
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final BluetoothDevice entity = mLeDevices.get(position);
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_bluetooth_binding_item, null);
            viewHolder.deviceName = (TextView) convertView
                    .findViewById(R.id.txtBlueName);
            viewHolder.deviceAddress = (TextView) convertView
                    .findViewById(R.id.txtBlueAddress);
            viewHolder.deviceDistance = (TextView) convertView
                    .findViewById(R.id.txtDistance);
            viewHolder.btnbindingDevice = (Button) convertView
                    .findViewById(R.id.btn_binding_device);
            convertView.setTag(viewHolder);
        }
        String tempMac = RCSPDeviceInfo.getBlueToothMac(device_id);
        if (!tempMac.equals(mLeDevices.get(position).getAddress())) {
            viewHolder.btnbindingDevice.setText("连接");
            viewHolder.btnbindingDevice.setBackgroundResource(R.drawable.rectangle_main_5px);//连接按钮的ui变化
            viewHolder.btnbindingDevice.setEnabled(true);
        } else {
            viewHolder.btnbindingDevice.setText("已绑定");
            viewHolder.btnbindingDevice.setTextColor(mActivity.getResources().getColor(R.color.rcbase_app_main));
            viewHolder.btnbindingDevice.setBackgroundResource(R.color.rcbase_white);
            viewHolder.btnbindingDevice.setEnabled(false);
        }

        viewHolder.btnbindingDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLeDevices.get(position) == null) {
                    return;
                }
                mActivity.postData(mLeDevices.get(position));
            }
        });

        if (entity.getName() != null && entity.getName().length() > 0) {
            viewHolder.deviceName.setText(entity.getName());
            viewHolder.deviceDistance.setText(mDistances.get(position) + "");
        } else {
            viewHolder.deviceName.setText("未找到设备");
        }
        if (entity.getAddress() != null && entity.getAddress().length() > 0) {
            viewHolder.deviceAddress.setText(entity.getAddress());
        } else {
            viewHolder.deviceAddress.setText("未找到设备地址");
        }
        convertView.setTag(viewHolder);
        return convertView;
    }

    public class ViewHolder {
        TextView deviceName; // 蓝牙设备名称
        TextView deviceAddress; // 蓝牙设备地址编号
        TextView deviceDistance; // 蓝牙设备地址编号
        Button btnbindingDevice; // 蓝牙设备地址编号
    }

}

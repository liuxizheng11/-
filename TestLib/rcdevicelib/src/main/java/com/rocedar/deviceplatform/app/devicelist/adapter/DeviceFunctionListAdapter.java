package com.rocedar.deviceplatform.app.devicelist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocedar.base.RCBaseConfig;
import com.rocedar.base.RCDialog;
import com.rocedar.base.RCImageShow;
import com.rocedar.base.RCTPJump;
import com.rocedar.base.RCToast;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.devicelist.DeviceFunctionListActivity;
import com.rocedar.deviceplatform.device.bluetooth.RCDeviceConfigUtil;
import com.rocedar.deviceplatform.dto.data.RCDeviceDataListDTO;
import com.rocedar.deviceplatform.request.impl.RCDeviceOperationRequestImpl;
import com.rocedar.deviceplatform.request.listener.RCRequestSuccessListener;
import com.rocedar.deviceplatform.sharedpreferences.RCSPDeviceInfo;

import java.util.ArrayList;
import java.util.List;

import static com.rocedar.deviceplatform.R.id.device_manager_wifi;

/**
 * Created by lxz on 17/2/13.
 */

public class DeviceFunctionListAdapter extends BaseAdapter {
    private DeviceFunctionListActivity mContext;
    private List<RCDeviceDataListDTO> mList = new ArrayList<>();

    private RCDialog newDialog;
    private int from_id;

    private RCDeviceOperationRequestImpl operationRequest;

    public DeviceFunctionListAdapter(DeviceFunctionListActivity mContext, List<RCDeviceDataListDTO> mList, int from) {
        this.mContext = mContext;
        this.mList = mList;
        this.from_id = from;
        operationRequest = RCDeviceOperationRequestImpl.getInstance(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_device_function_new, null);
            viewHolder.device_manager_iv = (ImageView) convertView.findViewById(R.id.device_manager_iv);
            viewHolder.device_manager_role = (TextView) convertView.findViewById(R.id.device_manager_role);
            viewHolder.device_manager_name = (TextView) convertView.findViewById(R.id.device_manager_name);
            viewHolder.device_manager_wifi = (TextView) convertView.findViewById(device_manager_wifi);
            viewHolder.device_manager_dont_binding = (TextView) convertView.findViewById(R.id.device_manager_dont_binding);
            viewHolder.data_from_status_binding_ok = (TextView) convertView.findViewById(R.id.data_from_status_binding_ok);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (RCBaseConfig.APPTAG == RCBaseConfig.APPTAG_DONGYA) {
            viewHolder.device_manager_wifi.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_main_sloke_2px));
            viewHolder.device_manager_wifi.setTextColor(mContext.getResources().getColor(R.color.rcbase_app_main));
            viewHolder.device_manager_dont_binding.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_main_5px));
        } else {
            viewHolder.device_manager_wifi.setBackground(mContext.getResources().getDrawable(R.drawable.btn_corner_rectange_white));
            viewHolder.device_manager_wifi.setTextColor(mContext.getResources().getColor(R.color.rcbase_app_text_default));
            viewHolder.device_manager_dont_binding.setBackground(mContext.getResources().getDrawable(R.drawable.btn_corner_rectange_black));

        }
        /**显示 设备图片*/
        RCImageShow.loadUrl(mList.get(position).getDevice_logo(),
                viewHolder.device_manager_iv, RCImageShow.IMAGE_TYPE_NINE);
        /**显示设备名称*/
        viewHolder.device_manager_name.setText(mList.get(position).getDevice_name());
        /**通过传入ID 判断*/
        switch (from_id) {
            /**我的设备列表*/
            case DeviceFunctionListActivity.FROM_MY_DEVICE:
                //判断是否显示 解除绑定（1是:显示解绑按钮 0否:不显示解绑按钮）
                if (mList.get(position).getBind() == 1) {
                    viewHolder.device_manager_dont_binding.setVisibility(View.VISIBLE);
                    viewHolder.device_manager_dont_binding.setText(mContext.getString(R.string.rcdevice_unbinding));
                } else {
                    viewHolder.device_manager_dont_binding.setVisibility(View.GONE);
                }
                //判断是否显示 配置wifi
                if (mList.get(position).getWifi_url().equals("")) {
                    viewHolder.device_manager_wifi.setVisibility(View.GONE);
                } else {
                    viewHolder.device_manager_wifi.setVisibility(View.VISIBLE);
                }
//                显示 是否有家人绑定
                if (mList.get(position).getRelation_name().equals("")) {
                    viewHolder.device_manager_role.setVisibility(View.GONE);
                } else {
                    viewHolder.device_manager_role.setVisibility(View.VISIBLE);
                    viewHolder.device_manager_role.setText(mList.get(position).getRelation_name());
                }

                break;
            /**数据来源*/
            case DeviceFunctionListActivity.FROM_DATA:
                //status	设备状态，0:未绑定 1:已绑定
                if (mList.get(position).getBind() == 1) {
                    //显示 已绑定  去测量
                    if (!mList.get(position).getMeasure_url().equals("")) {
                        viewHolder.data_from_status_binding_ok.setVisibility(View.VISIBLE);
                        viewHolder.device_manager_dont_binding.setVisibility(View.VISIBLE);
                        viewHolder.device_manager_dont_binding.setText(mContext.getString(R.string.rcdevice_go_measure));
                    } else {
                        //显示 已绑定
                        viewHolder.data_from_status_binding_ok.setVisibility(View.VISIBLE);
                        viewHolder.device_manager_dont_binding.setVisibility(View.GONE);
                    }
                    //显示 去测量
                } else if (mList.get(position).getBind() == -1) {
                    viewHolder.data_from_status_binding_ok.setVisibility(View.GONE);
                    viewHolder.device_manager_dont_binding.setVisibility(View.VISIBLE);
                    viewHolder.device_manager_dont_binding.setText(mContext.getString(R.string.rcdevice_go_measure));
                } else {
                    //显示 去绑定
                    viewHolder.data_from_status_binding_ok.setVisibility(View.GONE);
                    viewHolder.device_manager_dont_binding.setVisibility(View.VISIBLE);
                    viewHolder.device_manager_dont_binding.setText(mContext.getString(R.string.rcdevice_go_binding));
                }
                break;
        }
        /**点击 去测量 去绑定 解除绑定*/
        viewHolder.device_manager_dont_binding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (from_id) {
                    /**我的设备列表*/
                    case DeviceFunctionListActivity.FROM_MY_DEVICE:
                        /**解除绑定*/
                        newDialog = new RCDialog(mContext, new String[]{mContext.getString(R.string.rcdevice_hint), mContext.getString(R.string.rcdevice_do_unbinding_msg),
                                mContext.getString(R.string.rcdevice_cancel), mContext.getString(R.string.rcdevice_sure)},
                                null, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                operationRequest.doUnBinding(
                                        new RCRequestSuccessListener() {
                                            @Override
                                            public void requestSuccess() {
                                                //断开解绑设备的蓝牙连接
                                                if (RCDeviceConfigUtil.getBluetoothImpl(mContext,
                                                        mList.get(position).getDevice_id()) != null) {
                                                    RCDeviceConfigUtil.getBluetoothImpl(mContext,
                                                            mList.get(position).getDevice_id()).doDisconnect();
//                                             //从缓存中删除该设备的绑定记录（避免服务中再去获取数据）
                                                    RCSPDeviceInfo.removeBlueToothInfo(mList.get(position).getDevice_id());
                                                }
                                                mContext.initView();
                                            }

                                            @Override
                                            public void requestError(int status, String msg) {
                                                RCToast.Center(mContext, msg);
                                            }
                                        }, mList.get(position).getDevice_id(),
                                        mList.get(position).getDevice_no());
                                newDialog.dismiss();
                            }
                        });
                        newDialog.show();

                        break;
                    /**数据来源*/
                    case DeviceFunctionListActivity.FROM_DATA:
                        if (!mList.get(position).getMeasure_url().equals("")) {
                            if (mList.get(position).getBind() == -1 || mList.get(position).getBind() == 1) {
                                RCTPJump.ActivityJump(mContext, mList.get(position).getMeasure_url());
                            } else {
                                RCTPJump.ActivityJump(mContext, mList.get(position).getBind_url());
                            }
                        } else {
                            RCTPJump.ActivityJump(mContext, mList.get(position).getBind_url());
                        }
                        break;
                }

            }
        });
        /**跳转Wifi绑定页面*/
        viewHolder.device_manager_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCTPJump.ActivityJump(mContext, mList.get(position).getWifi_url());
            }
        });
        /**跳转设备详情页*/
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from_id==DeviceFunctionListActivity.FROM_MY_DEVICE) {
                    RCTPJump.ActivityJump(mContext, mList.
                            get(position).getData_url());
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView device_manager_iv;
        TextView device_manager_role;
        TextView device_manager_name;
        TextView device_manager_wifi;
        TextView device_manager_dont_binding;
        TextView data_from_status_binding_ok;
    }

}

package com.rocedar.deviceplatform.app.devicelist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rocedar.base.RCBaseConfig;
import com.rocedar.base.RCHandler;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.devicelist.adapter.DeviceFunctionListAdapter;
import com.rocedar.deviceplatform.app.view.MyListView;
import com.rocedar.deviceplatform.config.RCDeviceIndicatorID;
import com.rocedar.deviceplatform.dto.data.RCDeviceDataListDTO;
import com.rocedar.deviceplatform.request.impl.RCDeviceOperationRequestImpl;
import com.rocedar.deviceplatform.request.listener.RCDeviceGetDataListener;

import java.util.List;

/**
 * 作者：lxz
 * 日期：17/2/18 上午11:30
 * 版本：V1.0
 * 描述：Wifi 绑定页面
 * from_id 来源ID
 * FROM_DATA  数据来源
 * FROM_MY_DEVICE   我的设备
 * indicator_id  指标ID
 */
public class DeviceFunctionListActivity extends RCBaseActivity {
    private DeviceFunctionListAdapter listAdapter;
    /**
     * 查看其它设备
     */
    private TextView my_device_manager_add_binding;
    /**
     * 查看其它设备
     */
    private MyListView my_device_manager_listview;
    private int from_id;
    /**
     * 指标ID
     */
    private int indicator_id;
    /**
     * 来源于 数据来源
     */
    public static final int FROM_DATA = 1000;
    /**
     * 来源于 我的设备
     */
    public static final int FROM_MY_DEVICE = 1001;
    private RCDeviceOperationRequestImpl operationRequest;

    /**
     * 家人 ID
     */
    private long user_id;

    /**
     * 空页面
     */
    private TextView devices_dont_data_binding;
    private TextView devices_dont_data_tv;
    private ImageView devices_dont_data_iv;
    private LinearLayout devices_dont_data_ll;
    private View view_device_manager;

    /**
     * 数据来源
     *
     * @param context
     * @param indicator_id 指标ID
     */
    public static void gotoActivity(Context context, int indicator_id) {
        Intent intent = new Intent(context, DeviceFunctionListActivity.class);
        intent.putExtra("from_id", FROM_DATA);
        intent.putExtra("indicator_id", indicator_id);
        context.startActivity(intent);
    }

    /**
     * 家人数据来源
     *
     * @param context
     * @param indicator_id 指标ID
     * @param user_id      家人ID
     */
    public static void gotoActivity(Context context, int indicator_id, long user_id) {
        Intent intent = new Intent(context, DeviceFunctionListActivity.class);
        intent.putExtra("from_id", FROM_DATA);
        intent.putExtra("indicator_id", indicator_id);
        intent.putExtra("user_id", user_id);
        context.startActivity(intent);
    }

    /**
     * 我的设备
     *
     * @param context
     */
    public static void gotoActivity(Context context) {
        Intent intent = new Intent(context, DeviceFunctionListActivity.class);
        intent.putExtra("from_id", FROM_MY_DEVICE);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_function_list_main);
        if (!getIntent().hasExtra("from_id")) {
            finishActivity();
        }
        from_id = getIntent().getIntExtra("from_id", -1);
        indicator_id = getIntent().getIntExtra("indicator_id", -1);
        user_id = getIntent().getLongExtra("user_id", -1);
        operationRequest = RCDeviceOperationRequestImpl.getInstance(mContext);
        mRcHeadUtil.setLeftBack().setTitle(getString(from_id == FROM_MY_DEVICE ? R.string.rcdevice_my_device : R.string.rcdevice_data_from));
    }

    public void initView() {
        devices_dont_data_binding = (TextView) findViewById(R.id.devices_dont_data_binding);
        my_device_manager_add_binding = (TextView) findViewById(R.id.my_device_manager_add_binding);
        my_device_manager_listview = (MyListView) findViewById(R.id.my_device_manager_listview);
        devices_dont_data_ll = (LinearLayout) findViewById(R.id.devices_dont_data_ll);
        devices_dont_data_tv = (TextView) findViewById(R.id.devices_dont_data_tv);
        devices_dont_data_iv = (ImageView) findViewById(R.id.devices_dont_data_iv);
        view_device_manager = findViewById(R.id.view_device_manager);

        if (RCBaseConfig.APPTAG == RCBaseConfig.APPTAG_DONGYA) {
            my_device_manager_add_binding.setBackgroundColor(getResources().getColor(R.color.rcbase_app_main));
            my_device_manager_add_binding.setTextColor(getResources().getColor(R.color.rcbase_white));
            my_device_manager_add_binding.setText(getResources().getString(R.string.rcdevice_my_device_text_add));
            devices_dont_data_iv.setImageResource(R.mipmap.icon_devices_dont_data_dy);
            devices_dont_data_binding.setBackgroundColor(getResources().getColor(R.color.rcbase_app_main));
            devices_dont_data_tv.setVisibility(View.VISIBLE);
        } else {
            my_device_manager_add_binding.setBackground(getResources().getDrawable(R.drawable.btn_corner_rectange_white));
            my_device_manager_add_binding.setTextColor(getResources().getColor(R.color.rcbase_app_text_default));
            my_device_manager_add_binding.setText(getResources().getString(R.string.rcdevice_add_device));
            devices_dont_data_iv.setImageResource(R.mipmap.icon_devices_dont_data_n3);
            devices_dont_data_binding.setBackgroundResource(R.drawable.btn_rectange_main_color_n3);
            devices_dont_data_tv.setVisibility(View.GONE);
        }
        switch (from_id) {
            //数据来源
            case FROM_DATA:
                mRcHandler.sendMessage(RCHandler.START);
                operationRequest.getDeviceIndicatorList(new RCDeviceGetDataListener() {
                    @Override
                    public void getDataSuccess(List<RCDeviceDataListDTO> dtoList) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                        if (dtoList.size() > 0) {
                            my_device_manager_add_binding.setVisibility(View.VISIBLE);
                            my_device_manager_listview.setVisibility(View.VISIBLE);
                            devices_dont_data_ll.setVisibility(View.GONE);
                            view_device_manager.setVisibility(View.VISIBLE);
                            listAdapter = new DeviceFunctionListAdapter(DeviceFunctionListActivity.this, dtoList, from_id);
                            my_device_manager_listview.setAdapter(listAdapter);
                            listAdapter.notifyDataSetChanged();
                        } else {
                            my_device_manager_add_binding.setVisibility(View.GONE);
                            my_device_manager_listview.setVisibility(View.GONE);
                            devices_dont_data_ll.setVisibility(View.VISIBLE);
                            view_device_manager.setVisibility(View.GONE);
                            if (indicator_id == RCDeviceIndicatorID.Blood_Oxygen) {
                                devices_dont_data_tv.setVisibility(View.VISIBLE);
                                devices_dont_data_tv.setText(mContext.getString(R.string.rcdevice_device_blood_oxygen_no));
                                devices_dont_data_binding.setVisibility(View.GONE);
                            }

                        }
                    }

                    @Override
                    public void getDataError(int status, String msg) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }
                }, indicator_id, user_id);
                break;
            //我的设备
            case FROM_MY_DEVICE:
                mRcHandler.sendMessage(RCHandler.START);
                operationRequest.getDeviceUserBindList(new RCDeviceGetDataListener() {
                    @Override
                    public void getDataSuccess(List<RCDeviceDataListDTO> dtoList) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                        if (dtoList.size() > 0) {
                            my_device_manager_add_binding.setVisibility(View.VISIBLE);
                            my_device_manager_listview.setVisibility(View.VISIBLE);
                            devices_dont_data_ll.setVisibility(View.GONE);
                            view_device_manager.setVisibility(View.VISIBLE);
                            listAdapter = new DeviceFunctionListAdapter(DeviceFunctionListActivity.this, dtoList, from_id);
                            my_device_manager_listview.setAdapter(listAdapter);
                            listAdapter.notifyDataSetChanged();
                        } else {
                            my_device_manager_add_binding.setVisibility(View.GONE);
                            my_device_manager_listview.setVisibility(View.GONE);
                            devices_dont_data_ll.setVisibility(View.VISIBLE);
                            view_device_manager.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void getDataError(int status, String msg) {
                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    }
                });
                break;
        }
        //查看 其他设备
        my_device_manager_add_binding.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DeviceChooseListActivity.goActivity(mContext);
            }
        });
        //查看 其他设备
        devices_dont_data_binding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceChooseListActivity.goActivity(mContext);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }
}

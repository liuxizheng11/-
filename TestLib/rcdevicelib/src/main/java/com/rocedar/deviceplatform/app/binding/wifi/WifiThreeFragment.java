package com.rocedar.deviceplatform.app.binding.wifi;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocedar.base.RCHandler;
import com.rocedar.base.RCImageShow;
import com.rocedar.base.RCToast;
import com.rocedar.base.manger.RCBaseFragment;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.device.wifi.RCWifi;
import com.rocedar.deviceplatform.device.wifi.impl.RCWifiLeXinimpl;
import com.rocedar.deviceplatform.device.wifi.listener.RCWifiConfigListener;


/**
 * Created by lxz on 16/11/17.
 */

public class WifiThreeFragment extends RCBaseFragment {

    static {
        System.loadLibrary("LSWifiConfig");
    }

    private ImageView wifiThreeBg;
    private TextView wifi_name;
    private TextView wifi_on;
    private TextView wifi_connect;
    private TextView wifi_setting;
    private EditText wifi_passward;


    private boolean wifi_on_off = false;
    private String image;

    private int device_id;

    private RCWifi rcWifi;

    public static WifiThreeFragment newInstance(int device_id, String image) {
        Bundle args = new Bundle();
        args.putInt("device_id", device_id);
        args.putString("img", image);
        WifiThreeFragment fragment = new WifiThreeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi_three, null);
        image = getArguments().getString("img");
        device_id = getArguments().getInt("device_id");
        initView(view);
        return view;

    }

    private void initView(View view) {
        wifi_name = (TextView) view.findViewById(R.id.wifi_name);
        wifi_on = (TextView) view.findViewById(R.id.wifi_on);
        wifi_connect = (TextView) view.findViewById(R.id.wifi_connect);

        wifi_passward = (EditText) view.findViewById(R.id.wifi_passward);
        wifi_setting = (TextView) view.findViewById(R.id.wifi_setting);

        wifiThreeBg = (ImageView) view.findViewById(R.id.wifi_three_bg);

        RCImageShow.loadUrl(image, wifiThreeBg, RCImageShow.IMAGE_TYPE_ALBUM);

        if (WifiConfigUtil.isWifiEnabled(mActivity)) {
            wifi_name.setText(WifiConfigUtil.getConnectWifiName(mActivity));
        }
        wifi_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiConfigUtil.openWifiSetting(mActivity);
            }
        });


        wifi_setting.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        wifi_setting.getPaint().setAntiAlias(true);//抗锯齿

        wifi_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable img;
                if (wifi_on_off) {
                    img = mActivity.getResources().getDrawable(R.mipmap.ic_wifi_notselected);
                    img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
                    wifi_on.setCompoundDrawables(img, null, null, null); //设置左图标
                    wifi_on_off = false;
                } else {
                    img = mActivity.getResources().getDrawable(R.mipmap.ic_wifi_selected);
                    img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
                    wifi_on.setCompoundDrawables(img, null, null, null); //设置左图标
                    wifi_on_off = true;
                }
            }
        });


        wifi_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wifi_name.getText().toString().trim().equals("")) {
                    RCToast.Center(mActivity, mActivity.getString(R.string.rcdevice_wifi_name_erro), false);
                    return;
                }
                if (wifi_passward.getText().toString().trim().equals("")) {
                    RCToast.Center(mActivity, mActivity.getString(R.string.rcdevice_wifi_passward_erro), false);
                    return;
                }
                if (!wifi_on_off) {
                    RCToast.Center(mActivity, mActivity.getString(R.string.rcdevice_wifi_on_erro), false);
                    return;
                }
                mRcHandler.sendMessage(RCHandler.START, "WIFI设置中");
                /**
                 * 通过device_id 调用不同WIFI
                 */
                switch (device_id) {
                    case RCDeviceDeviceID.LX_BF:
                    case RCDeviceDeviceID.LX_WIFI_BP:
                        rcWifi = RCWifiLeXinimpl.getInstance(mActivity);
                        rcWifi.configWifi(new RCWifiConfigListener() {
                            @Override
                            public void configOk() {
                                mRcHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        RCToast.Center(mActivity, mActivity.getString(R.string.rcdevice_wifi_success), false);
                                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                                        mActivity.finish();
                                    }
                                });
                            }

                            @Override
                            public void error(int status, final String msg) {
                                mRcHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        RCToast.Center(mActivity, msg, false);
                                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                                    }
                                });
                            }
                        }, wifi_name.getText().toString().trim(), wifi_passward.getText().toString().trim());
                        break;
                }

            }
        });

    }
}

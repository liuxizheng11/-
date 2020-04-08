package com.rocedar.deviceplatform.app.binding;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocedar.base.RCImageShow;
import com.rocedar.deviceplatform.R;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * @author liuyi
 * @date 2017/7/21
 * @desc 多角色设备绑定提示弹框
 * @veison V3420
 */

public class DeviceBindStatusDialog extends Dialog {
    private TextView tv_device_icon_one;
    private TextView tv_device_icon_two;
    private ImageView iv_device_icon_one;
    private ImageView iv_device_icon_two;
    private JSONObject result;

    public DeviceBindStatusDialog(Context context, JSONObject result) {
        super(context);
        this.result = result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_device_bind_status);
        initView();
    }

    private void initView() {
        tv_device_icon_one = (TextView) findViewById(R.id.tv_device_icon_one);
        tv_device_icon_two = (TextView) findViewById(R.id.tv_device_icon_two);
        iv_device_icon_one = (ImageView) findViewById(R.id.iv_device_icon_one);
        iv_device_icon_two = (ImageView) findViewById(R.id.iv_device_icon_two);

        findViewById(R.id.tv_device_icon_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        JSONArray err_msg = result.optJSONArray("err_msg");
        for (int i = 0; i < err_msg.length(); i++) {
            JSONObject object = err_msg.optJSONObject(i);
            String msg = object.optString("msg");
            String img = object.optString("img");
            if (i == 0) {
                tv_device_icon_one.setText(msg);
                RCImageShow.loadUrl(img, iv_device_icon_one, RCImageShow.IMAGE_TYPE_HEAD);
            }

            if (i == 1) {
                tv_device_icon_two.setText(msg);
                RCImageShow.loadUrl(img, iv_device_icon_two, RCImageShow.IMAGE_TYPE_HEAD);
            }
        }

    }

}

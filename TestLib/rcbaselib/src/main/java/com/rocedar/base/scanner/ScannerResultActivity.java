package com.rocedar.base.scanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rocedar.base.R;
import com.rocedar.base.RCTPJump;
import com.rocedar.base.manger.RCBaseActivity;

/**
 * @author liuyi
 * @date 2017/4/25
 * @desc 扫描结果
 * @veison V3.3.40新增
 */
public class ScannerResultActivity extends RCBaseActivity {

    public static void goActivity(Context context) {
        Intent intent = new Intent(context, ScannerResultActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_result);
        mRcHeadUtil.setTitle(getString(R.string.base_scan_result)).setLeftBack();
        //取消
        findViewById(R.id.tv_scan_result_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        //绑定
        findViewById(R.id.tv_scan_result_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCTPJump.ActivityJump(mContext,"rctp://android##com.rocedar.deviceplatform.app.devicelist.DeviceChooseListActivity##{}");
                finishActivity();
            }
        });
    }
}

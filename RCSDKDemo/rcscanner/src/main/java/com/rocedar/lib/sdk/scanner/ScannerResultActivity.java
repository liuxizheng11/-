package com.rocedar.lib.sdk.scanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.unit.RCDrawableUtil;


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
        finish();
        setContentView(R.layout.activity_scanner_result);
        mRcHeadUtil.setTitle(getString(R.string.rc_scan_result)).setLeftBack();
        //取消
        findViewById(R.id.tv_scan_result_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        findViewById(R.id.tv_scan_result_sure).setBackground(
                DrawableUtil.rectangle_main_sloke_2px(mContext)
        );
        //绑定
        findViewById(R.id.tv_scan_result_sure).setBackground(
                RCDrawableUtil.getMainColorDrawableBaseRadius(mContext)
        );
        findViewById(R.id.tv_scan_result_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
    }
}

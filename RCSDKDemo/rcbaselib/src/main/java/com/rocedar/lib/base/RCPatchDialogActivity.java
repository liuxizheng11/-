package com.rocedar.lib.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.manage.RCSDKManage;
import com.rocedar.lib.base.unit.RCDialog;

/**
 * Created by shenhuniurou
 * on 2017/1/3.
 */

public class RCPatchDialogActivity extends RCBaseActivity {


    public static void goActivity(Context context) {
        Intent intent = new Intent(context, RCPatchDialogActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setNotAddHead(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_view_none);
        showPatchDialog();
    }

    private RCDialog rcDialog;

    private void showPatchDialog() {
        rcDialog = new RCDialog(mContext, new String[]{"错误", "发生了点意外，请重启应用后重试。"
                , null, null}, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCSDKManage.getScreenManger().AppExit(mContext);
                rcDialog.dismiss();
                restart();
            }
        });
        rcDialog.show();
    }


    private void restart() {
        RCSDKManage.getScreenManger().AppExit(mContext);
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);
        finish();
    }


}

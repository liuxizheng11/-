package com.rocedar.sdk.familydoctor.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.fragment.RCFDSpecialistRecordDetailFragment;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/30 下午5:26
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDSpecialistRecordDetailActivity extends RCBaseActivity {

    public static void goActivity(Context context, String recordID, String phone, String deviceNo) {
        Intent intent = new Intent(context, RCFDSpecialistRecordDetailActivity.class);
        intent.putExtra("record_id", recordID);
        intent.putExtra("phone", phone);
        intent.putExtra("device_no", deviceNo);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_fd_view_framelayout);
        mRcHeadUtil.setTitle(getString(R.string.rc_fd_consult_record));
        showContent(R.id.rc_fd_view_framelayout_main,
                RCFDSpecialistRecordDetailFragment.newInstance(
                        getIntent().getStringExtra("record_id"),
                        getIntent().getStringExtra("phone"),
                        getIntent().getStringExtra("device_no")
                ));
    }

}

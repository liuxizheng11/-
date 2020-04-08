package com.rocedar.base.scanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.rocedar.base.R;
import com.rocedar.base.manger.RCBaseActivity;


/**
 * @author liuyi
 * @date 2017/2/11
 * @desc 二维码扫瞄
 * @veison V1.0
 */
public class CaptureActivity extends RCBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sweep);
        mRcHeadUtil.setTitle(getString(R.string.base_scan));
//        TextView backTextView = (TextView) view.findViewById(R.id.rcbase_view_head_back);
//        Drawable icoDrawable;
//        if (RCBaseConfig.APPTAG == RCBaseConfig.APPTAG_DONGYA) {
//            icoDrawable = getResources().getDrawable(R.mipmap.dy_arrow_left);
//        } else {
//            icoDrawable = getResources().getDrawable(R.mipmap.n3_arrow_left);
//        }
//        icoDrawable.setBounds(0, 0, icoDrawable.getMinimumWidth(),
//                icoDrawable.getMinimumHeight());
//        backTextView.setCompoundDrawables(icoDrawable, null, null, null);
//        backTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mActivity.finish();
//            }
//        });

        CaptureFragment captureFragment = new CaptureFragment();
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_zxing_container, captureFragment).commit();
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            CaptureActivity.this.setResult(RESULT_OK, resultIntent);
            CaptureActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            CaptureActivity.this.setResult(RESULT_OK, resultIntent);
            CaptureActivity.this.finish();
        }
    };
}
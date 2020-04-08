package sdk.lib.rocedar.com.rcsdkdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rocedar.lib.base.dialog.RCSpeechDialog;
import com.rocedar.lib.base.manage.RCBaseActivity;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/10/30 3:50 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class SpeechTest2Activity extends RCBaseActivity {

    public static void goActivity(Context context) {
        Intent intent = new Intent(context, SpeechTest2Activity.class);
        context.startActivity(intent);
    }

    private Button mStartBtn;
    private EditText mLogTv;

    private RCSpeechDialog speechDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);
        mStartBtn = findViewById(R.id.and_speech_btn);
        mLogTv = findViewById(R.id.and_speech_tv);

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechDialog = new RCSpeechDialog(mContext, mLogTv);
                speechDialog.show();
            }
        });
//        if (!RCSpeechUtil.hasFunction(this)) {
//            finish();
//        }
    }


}



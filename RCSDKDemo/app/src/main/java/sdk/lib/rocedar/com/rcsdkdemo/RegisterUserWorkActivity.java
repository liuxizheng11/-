package sdk.lib.rocedar.com.rcsdkdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.sdk.assessment.fragment.RegisterUserAssessmentFragment;

/**
 * 作者：lxz
 * 日期：2018/5/17 下午3:18
 * 版本：V1.0
 * 描述：个人信息---工作、血压、血糖 页面
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RegisterUserWorkActivity extends RCBaseActivity {
    
    public static void goActivity(Context context){
        Intent intent = new Intent(context, RegisterUserWorkActivity.class);
        context.startActivity(intent);
    }
    

    private RegisterUserAssessmentFragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_view_framelayout);
        mRcHeadUtil.setTitle("完善信息(3/3)");
        mRcHeadUtil.setRightButton("跳过", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到主页
                startActivity(new Intent(mContext, MainActivity.class));
                finishActivity();
            }
        });
        showContent(R.id.rc_view_framelayout_main,
                fragment = RegisterUserAssessmentFragment.newInstance());
        fragment.setListener(new IRCPostListener() {
            @Override
            public void getDataSuccess() {
                startActivity(new Intent(mContext, MainActivity.class));
                finishActivity();
            }

            @Override
            public void getDataError(int i, String s) {

            }
        });

    }


}

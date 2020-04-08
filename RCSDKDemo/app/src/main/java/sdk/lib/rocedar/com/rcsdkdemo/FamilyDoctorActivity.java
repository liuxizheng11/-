package sdk.lib.rocedar.com.rcsdkdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.rcoedar.lib.sdk.yunxin.RCYXUtil;
import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.sdk.familydoctor.app.RCFDAddUserActivity;
import com.rocedar.sdk.familydoctor.app.RCFDMainActivity;
import com.rocedar.sdk.familydoctor.app.RCFDPatientListActivity;
import com.rocedar.sdk.familydoctor.app.RCFDSpecialistActivity;
import com.rocedar.sdk.familydoctor.app.RCFDSpecialistEvaluateActivity;
import com.rocedar.sdk.familydoctor.app.RCFDSpecialistIntroducedActivity;
import com.rocedar.sdk.familydoctor.app.RCFDSpecialistRecordDetailActivity;
import com.rocedar.sdk.familydoctor.app.RCMingYiCompleteMaterialActivity;
import com.rocedar.sdk.familydoctor.app.RCMingYiDoctorDetailActivity;
import com.rocedar.sdk.familydoctor.app.RCMingYiDoctorListActivity;
import com.rocedar.sdk.familydoctor.app.RCMingYiOrderParticularsActivity;
import com.rocedar.sdk.shop.app.PayWebViewActivity;

import java.util.ArrayList;
import java.util.List;

import sdk.lib.rocedar.com.rcsdkdemo.adapter.MainListAdapter;
import sdk.lib.rocedar.com.rcsdkdemo.dto.FunctionListDTO;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/7/18 上午10:02
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class FamilyDoctorActivity extends RCBaseActivity {


    public static void goActivity(Context context) {
        Intent intent = new Intent(context, FamilyDoctorActivity.class);
        context.startActivity(intent);
    }

    private ListView listView;
    private MainListAdapter adapter;

    private List<FunctionListDTO> functionListDTOS = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_view_listview);
        mRcHeadUtil.setTitle("健康服务（家庭医生）");
        listView = findViewById(R.id.rc_view_listview);

        initMain();
        initWWZ();
        initMY();
        initMaterial();
        adapter = new MainListAdapter(mContext, functionListDTOS);
        listView.setAdapter(adapter);
    }

    private void initMain() {
        //第一类
        functionListDTOS.add(new FunctionListDTO("主页面"));
        functionListDTOS.add(new FunctionListDTO("V1.0版本（微问诊）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCFDMainActivity.goActivity(mContext, "",
                        "", "");
            }
        }));

        functionListDTOS.add(new FunctionListDTO("V2.0版本（微问诊、名医）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCFDMainActivity.goActivity(mContext, RCFDMainActivity.V2);
            }
        }));
        functionListDTOS.add(new FunctionListDTO("V3.0版本（微问诊、名医）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCFDMainActivity.goActivity(mContext, RCFDMainActivity.V3);
            }
        }));
    }

    private void initWWZ() {
        //第二类
        functionListDTOS.add(new FunctionListDTO("家庭医生（微问诊）"));
        functionListDTOS.add(new FunctionListDTO("专科医生列表", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCFDSpecialistActivity.goActivity(mContext, "", "");
            }
        }));
        functionListDTOS.add(new FunctionListDTO("专科医生详情（指定医生：ajx'安继新'）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCFDSpecialistIntroducedActivity.goActivity(mContext, "ajx", "", "");
            }
        }));
        functionListDTOS.add(new FunctionListDTO("专科医生评价（指定医生：ajx'安继新' 指定记录ID：459720）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCFDSpecialistEvaluateActivity.goActivity(mContext, "459720", "ajx",
                        "http://182.139.134.107:2226/group1/M00/03/04/oYYBAFXCxqOAXpPiAAByrfe5Uk8998.jpg",
                        "安继新", "主治医师", "全科医生");
            }
        }));
        functionListDTOS.add(new FunctionListDTO("咨询记录查看（指定咨询记录ID459720）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RCFDSpecialistRecordDetailActivity.goActivity(mContext, "459720", "", "");
            }
        }));
//        functionListDTOS.add(new FunctionListDTO("续费领券", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = "rctp://android##com.rocedar.sdk.familydoctor.app.ShopShowActivity##{\"url\":\"/shop/goods/coupon/\"}";
//                RCTPJump.ActivityJump(mContext, url);
//            }
//        }));
    }

    private void initMY() {
        //第三类
        functionListDTOS.add(new FunctionListDTO("名医"));
        functionListDTOS.add(new FunctionListDTO("名医医生列表", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCMingYiDoctorListActivity.goActivity(mContext);
            }
        }));
        functionListDTOS.add(new FunctionListDTO("名医医生详情（指定医生ID：23976）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCMingYiDoctorDetailActivity.goActivity(mContext, "23976" +
                        "");
            }
        }));
        functionListDTOS.add(new FunctionListDTO("名医医生订单详情（指定订单ID：1531267062）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCMingYiOrderParticularsActivity.goActivity(mContext, 1531267062, 1000);
            }
        }));
        functionListDTOS.add(new FunctionListDTO("名医医生完善病人资料（指定订单ID：1531267058 病人ID：115127171083979662）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCMingYiCompleteMaterialActivity.goActivity(mContext, "1531267058", "115127171083979662");
            }
        }));
        functionListDTOS.add(new FunctionListDTO("支付跳转测试（指定订单ID：1531267062）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayWebViewActivity.goActivity(mContext, 1531267062 + "");
            }
        }));
        functionListDTOS.add(new FunctionListDTO("视频连接测试（固定用户给'测试8'医生拨打）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RCYXUtil().startAdvisory(mContext, "d779c8715effadfe13a00a4bd4ad3bf9"
                        , "324020211f39cb94d366e7a683595b3b", "c75ae9fc60fada7567b2c5de3593b429",
                        "医生名称", "");
            }
        }));
    }


    private void initMaterial() {
        //第四类
        functionListDTOS.add(new FunctionListDTO("患者"));
        functionListDTOS.add(new FunctionListDTO("患者列表", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCFDPatientListActivity.goActivity(mContext, 1001);
            }
        }));
        functionListDTOS.add(new FunctionListDTO("添加患者", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCFDAddUserActivity.goActivity(mContext);
            }
        }));
    }


}

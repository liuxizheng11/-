package sdk.lib.rocedar.com.rcsdkdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.rcoedar.sdk.healthclass.app.RCHealthClassMainActivity;
import com.rocedar.lib.base.CrashListActivity;
import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.permission.AcpListener;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.base.unit.RCPermissionUtil;
import com.rocedar.lib.base.unit.RCTPJump;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.base.unit.crash.SdcardConfig;
import com.rocedar.lib.base.userinfo.RCSPUserInfo;
import com.rocedar.lib.sdk.scanner.CaptureActivity;
import com.rocedar.lib.sdk.scanner.CodeUtils;
import com.rocedar.lib.sdk.share.share.ShareDialog;
import com.rocedar.sdk.assessment.RCAssessmentListActivity;
import com.rocedar.sdk.familydoctor.app.RCXunYiInquiryActivity;
import com.rocedar.sdk.familydoctor.app.RCXunYiUserPerfectActivity;

import java.util.ArrayList;
import java.util.List;

import sdk.lib.rocedar.com.rcsdkdemo.adapter.MainListAdapter;
import sdk.lib.rocedar.com.rcsdkdemo.dto.FunctionListDTO;

public class MainActivity extends RCBaseActivity {

    private void initBaseFunction() {
        //第一类
        functionListDTOS.add(new FunctionListDTO("基本设置"));
        functionListDTOS.add(new FunctionListDTO("主题设置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemeSettingActivity.goActivity(mContext);
            }
        }));

        functionListDTOS.add(new FunctionListDTO("注销TOKEN", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCSPUserInfo.setLastSDKToken("");
                RCSPUserInfo.setLastAPIToken("", -1);
            }
        }));
    }


    private void initModeFunction() {
        //第二类
        functionListDTOS.add(new FunctionListDTO("SDK模块功能"));
        functionListDTOS.add(new FunctionListDTO("健康服务（家庭医生）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FamilyDoctorActivity.goActivity(mContext);
            }
        }));
        functionListDTOS.add(new FunctionListDTO("商城", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopActivity.goActivity(mContext);

            }
        }));
        functionListDTOS.add(new FunctionListDTO("健康课堂", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCHealthClassMainActivity.goActivity(mContext);

            }
        }));
        functionListDTOS.add(new FunctionListDTO("健康测评列表", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCAssessmentListActivity.goActivity(mContext, "", "");
            }
        }));
        functionListDTOS.add(new FunctionListDTO("在线医生", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCXunYiUserPerfectActivity.goActivity(mContext);
            }
        }));
        functionListDTOS.add(new FunctionListDTO("图文问诊", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCXunYiInquiryActivity.goActivity(mContext);
            }
        }));
        functionListDTOS.add(new FunctionListDTO("ITing", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCPermissionUtil.getPremission(mContext, new AcpListener() {
                    @Override
                    public void onGranted() {
//                        FunctionActivity.goActivity(mContext);
//                        MainTestActivity.goActivity(mContext);
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        RCToast.Center(mContext, "权限拒绝，请在设置中开启定位", false);
                    }
                }, Manifest.permission.ACCESS_FINE_LOCATION);

            }
        }));
        functionListDTOS.add(new FunctionListDTO("ITing", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCPermissionUtil.getPremission(mContext, new AcpListener() {
                    @Override
                    public void onGranted() {
//                        FunctionActivity.goActivity(mContext);
//                        MainTestActivity.goActivity(mContext);
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        RCToast.Center(mContext, "权限拒绝，请在设置中开启定位", false);
                    }
                }, Manifest.permission.ACCESS_FINE_LOCATION);

            }
        }));
    }


    private void initUtilFunction() {
        //第四类
        functionListDTOS.add(new FunctionListDTO("工具模块功能"));
        functionListDTOS.add(new FunctionListDTO("图片工具类（图片及头像选择及预览）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoActivity.goActivity(mContext);
            }
        }));
        functionListDTOS.add(new FunctionListDTO("超大图片加载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageShowActivity.goActivity(mContext);
            }
        }));

        functionListDTOS.add(new FunctionListDTO("扫一扫", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //相机权限
                RCPermissionUtil.getPremission(mContext, new AcpListener() {
                    @Override
                    public void onGranted() {
                        Intent intent = new Intent(mContext, CaptureActivity.class);
                        startActivityForResult(intent, REQUEST_CODE);
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        RCToast.Center(mContext, "权限拒绝，请在设置中开启相机权限", false);
                    }
                }, Manifest.permission.CAMERA);
            }
        }));
        functionListDTOS.add(new FunctionListDTO("WebView与JS交互测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCTPJump.ActivityJump(mContext, "" +
                        "rctp://h5##file:///android_asset/test.html##null");
            }
        }));
        functionListDTOS.add(new FunctionListDTO("分享", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareDialog(mContext,
                        "分享测试", "分享测试", "http://www.baidu.com",
                        "http://img.dongya.rocedar.com//101/u/m/201807/fcf6e7b021a2d25b.png!nine",
                        "[\"WX\",\"Qzone\",\"QQ\"]").show();
//                new ShareDialog(mContext, convertViewToBitmap(listView)).show();
            }
        }));
        functionListDTOS.add(new FunctionListDTO("分享2", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCTPJump.ActivityJump(mContext, "http://192.168.18.25/rest/3.0/dongya/health/info/100244/?app_key=2f12ea3cd4e86a387b32746a&title=低薪白领更易患高血压 每周运动3次可预防&desc=");
            }
        }));


    }


    private void initSettingFunction() {
        //第五类
        functionListDTOS.add(new FunctionListDTO("设置及其他功能"));

        functionListDTOS.add(new FunctionListDTO("清除图片缓存:" + RCImageShow.getImageDiskCacheSize(mContext), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCImageShow.clearImageDiskCache(mContext);
            }
        }));

//        functionListDTOS.add(new FunctionListDTO("Glide配置测试", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                GlideTestActivity.goActivity(mContext);
//            }
//        }));

        functionListDTOS.add(new FunctionListDTO("异常捕获测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = null;
                String[] split = str.split(",");
            }
        }));
        functionListDTOS.add(new FunctionListDTO("异常列表", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrashListActivity.goActivity(mContext);
            }
        }));
        functionListDTOS.add(new FunctionListDTO("清空异常日志", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SdcardConfig.clearList();
            }
        }));
    }

    private void initSignFunction() {
        //第六类
        functionListDTOS.add(new FunctionListDTO("单功能点测试"));

        functionListDTOS.add(new FunctionListDTO("注册问卷填写", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUserWorkActivity.goActivity(mContext);

            }
        }));
        functionListDTOS.add(new FunctionListDTO("每日一题", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                RegisterUserWorkActivity.goActivity(mContext);
                RCTPJump.ActivityJump(mContext, "http://dongya.rocedar.com/rest/3.0/activity/true/false/page/");
            }
        }));

        functionListDTOS.add(new FunctionListDTO("拼团活动（H5）",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RCTPJump.ActivityJump(mContext, "rctp://h5##activity/collage/index/##1000");
                    }
                }));


        functionListDTOS.add(new FunctionListDTO("语音识别", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpeechTestActivity.goActivity(mContext);
            }
        }));
        functionListDTOS.add(new FunctionListDTO("语音识别2", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpeechTest2Activity.goActivity(mContext);
            }
        }));


//        functionListDTOS.add(new FunctionListDTO("为用户绑定手机号（指定手机号：13128835427）", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RCFDConfigUtil.setPhoneNumber(mContext, "13128835427");
//            }
//        }));

    }


    private ListView listView;
    private MainListAdapter adapter;

    private List<FunctionListDTO> functionListDTOS = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_view_listview);
        mRcHeadUtil.setTitle("SDK DEMO ALL").setLeftButtonGone();
        listView = findViewById(R.id.rc_view_listview);
        initModeFunction();
        initBaseFunction();
        initSignFunction();
        initUtilFunction();
        initSettingFunction();
        adapter = new MainListAdapter(mContext, functionListDTOS);
        listView.setAdapter(adapter);
//        RCSDKManage.getInstance().setToken("b6b29e3d622b5b51640bf77aa5f0a658");
//        RCSPUserInfo.setLastAPIToken("b6b29e3d622b5b51640bf77aa5f0a658", 123123123);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private final int REQUEST_CODE = 20001;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            /**
             * 处理二维码扫描结果
             */
            case REQUEST_CODE:
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        String result = bundle.getString(CodeUtils.RESULT_STRING);
                        RCTPJump.ActivityJump(mContext, result, "");
                        /** 扫描成功－协议解析*/
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        RCToast.Center(mContext, "解析二维码失败", false);
                    }
                }
                break;


        }
    }


//    /**
//     * 清除Glide磁盘缓存，自己获取缓存文件夹并删除
//     */
//    public void clearGlideCacheDisk(final Context context) {
//        try {
//
//            if (Looper.myLooper() == Looper.getMainLooper()) {
//                //清除内存缓存 在UI主线程中进行
//                Glide.get(context).clearMemory();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //清理磁盘缓存 需要在子线程中执行
//                        Glide.get(context).clearDiskCache();
//                    }
//                }).start();
//            } else {
//                Glide.get(context).clearDiskCache();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//    }

}

package app.phj.com.testlib;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rocedar.base.RCBaseManage;
import com.rocedar.base.RCLog;
import com.rocedar.base.RCToast;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;
import com.rocedar.base.scanner.CodeUtils;
import com.rocedar.base.scanner.CaptureActivity;
import com.rocedar.base.scanner.ZXingLibrary;
import com.rocedar.deviceplatform.dto.data.RCDeviceDataListTypeDTO;
import com.rocedar.deviceplatform.request.impl.RCDeviceOperationRequestImpl;
import com.rocedar.deviceplatform.request.listener.RCDeviceDataListTypeListener;

import java.util.List;

public class MainActivity extends RCBaseActivity {
    private String TAG = getClass().getSimpleName();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ImageView imageView1 = (ImageView) findViewById(R.id.image_1);
//        ImageView imageView2 = (ImageView) findViewById(R.id.image_2);
//        ImageView imageView3 = (ImageView) findViewById(R.id.image_3);
//        ImageView imageView4 = (ImageView) findViewById(R.id.image_4);
//        ImageView imageView5 = (ImageView) findViewById(R.id.image_5);
//        ImageView imageView6 = (ImageView) findViewById(R.id.image_6);
        final TextView textview = (TextView) findViewById(R.id.textview);
//
//        String[] imgIds = new String[]{"http://img.taopic.com/uploads/allimg/140326/235113-1403260I05466.jpg"
//                                        ,"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=52754150,1809785422&fm=116&gp=0.jpg"};
//        GlideUtil.loadImage(this,imgIds[0],imageView1,GlideUtil.IMAGE_TYPE_ORIGINAL,0, R.mipmap.ic_launcher);
//        GlideUtil.loadImage(this,imgIds[0],imageView2,GlideUtil.IMAGE_TYPE_CIRCLE);
//        GlideUtil.loadImage(this,imgIds[0],imageView3,GlideUtil.IMAGE_TYPE_CIRCLE_CORNER);
//        GlideUtil.loadImage(this,imgIds[1],imageView4,GlideUtil.IMAGE_TYPE_GIF,0, R.mipmap.ic_launcher);
//        GlideUtil.loadImage(this,imgIds[1],imageView5,GlideUtil.IMAGE_TYPE_GIF_THUMBNAIL);
//
//        try {
//            textview.setText(GlideUtil.getTotalCacheSize(this));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        textview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                GlideUtil.clearDiskCache(MainActivity.this);
//                GlideUtil.clearMemory(MainActivity.this);
//                try {
//                    textview.setText(GlideUtil.getTotalCacheSize(MainActivity.this));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        //绑定
//        RCDeviceOperationRequestImpl.getInstance(this).doOAuth2Binding(new RCDeviceRequestListener() {
//            @Override
//            public void requestSuccess() {
//                RCLog.d(TAG,"chenggong");
//            }
//
//            @Override
//            public void requestError(int status, String msg) {
//                RCLog.d(TAG,msg);
//            }
//        },0001,"12345678");
//        //解绑
//        RCDeviceOperationRequestImpl.getInstance(this).doUnBinding(new RCDeviceRequestListener() {
//            @Override
//            public void requestSuccess() {
//                RCLog.d(TAG,"chenggong");
//            }
//
//            @Override
//            public void requestError(int status, String msg) {
//                RCLog.d(TAG,msg);
//            }
//        },000);
        //需要在使用前初始化
        RCBaseManage.getInstance().init(this);
        ZXingLibrary.initDisplayOpinion(this);
        RCSPBaseInfo.setLoginUserInfo(1,"6dfd9a3fef451e18e08ed3b450809dee");
        //获取设备类型
        RCDeviceOperationRequestImpl.getInstance(this).getDeviceDataType(new RCDeviceDataListTypeListener() {
            @Override
            public void getDataSuccess(List<RCDeviceDataListTypeDTO> dtoList) {
                RCLog.josn(TAG,dtoList.toString());
            }

            @Override
            public void getDataError(int status, String msg) {
                RCLog.d(TAG,msg);
            }
        });

//        startActivity(new Intent(this, DeviceChooseListActivity.class));
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,CaptureActivity.class);
                startActivityForResult(intent, 1000);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        二维码扫描
        if (requestCode == 1000) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    /** 扫描成功－协议解析*/
                RCToast.Center(this, result, false);
                    RCLog.d(TAG, result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    RCToast.Center(this, "解析二维码失败", false);
                }
            }
        }
    }
}

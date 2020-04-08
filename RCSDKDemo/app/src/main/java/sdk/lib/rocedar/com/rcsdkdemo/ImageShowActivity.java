package sdk.lib.rocedar.com.rcsdkdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.lib.base.view.subscaleview.ImageSource;
import com.rocedar.lib.base.view.subscaleview.RCSubsamplingScaleImageView;
import com.rocedar.lib.sdk.glide.RCGlideDownListener;
import com.rocedar.lib.sdk.glide.RCGlideUtil;

import java.io.File;

/**
 * 项目名称：瑰柏SDK-健康服务（家庭医生）
 * <p>
 * 作者：phj
 * 日期：2018/8/30 下午3:19
 * 版本：V1.1.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class ImageShowActivity extends RCBaseActivity {

    public static void goActivity(Context context) {
        Intent intent = new Intent(context, ImageShowActivity.class);
        context.startActivity(intent);
    }

    private ImageView imageView1;
    private RCSubsamplingScaleImageView imageView2;
    private Button button;


    String url1 = "/p/ta/header/1000.png";
    String url2 = "/p/ta/1011.png";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);

        button = findViewById(R.id.button1);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);

        RCImageShow.loadUrl(url1, imageView1, RCImageShow.IMAGE_TYPE_ALBUM);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://img.dongya.rocedar.com/p/ta/1010.png!album";
                new RCGlideUtil().downloadImage(mContext, url, new RCGlideDownListener() {
                    @Override
                    public void onLoadFailed(String info) {
                        RCLog.e(info);
                    }

                    @Override
                    public void onResourceReady(File resource) {
                        imageView2.setImage(ImageSource.uri(resource.getAbsolutePath()));
                    }
                });
            }
        });

    }


}

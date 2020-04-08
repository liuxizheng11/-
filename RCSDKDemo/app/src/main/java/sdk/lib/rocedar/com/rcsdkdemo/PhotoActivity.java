package sdk.lib.rocedar.com.rcsdkdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.base.unit.RCPhotoChooseUtil;
import com.rocedar.lib.base.view.RCChooseImageView;
import com.rocedar.lib.sdk.rcgallery.dto.RCPhotoDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/7/17 上午11:45
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class PhotoActivity extends RCBaseActivity {


    public static void goActivity(Context context) {
        Intent intent = new Intent(context, PhotoActivity.class);
        context.startActivity(intent);
    }

    private ImageView imageView;
    private Button button1;
    private Button button2;
    private Button button3;
    private RCChooseImageView chooseImageView;
    private TextView textViewButton;
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        mRcHeadUtil.setTitle("图片工具类");

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        imageView = findViewById(R.id.photo_image);
        chooseImageView = findViewById(R.id.choose_image_view);
        textViewButton = findViewById(R.id.text);
        textView = findViewById(R.id.text2);
        button3 = findViewById(R.id.button3);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RCPhotoChooseUtil(mContext).goChoose(1, new RCPhotoChooseUtil.ChooseAlbumListener() {
                    @Override
                    public void over(List<String> chooseList) {
                        if (chooseList.size() > 0) {
                            RCImageShow.loadFileImage(chooseList.get(0), imageView);
                        }
                    }
                });
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RCPhotoChooseUtil(mContext).goChooseHead(new RCPhotoChooseUtil.ChooseAlbumListener() {
                    @Override
                    public void over(List<String> chooseList) {
                        if (chooseList.size() > 0) {
                            RCImageShow.loadFileImage(chooseList.get(0), imageView);
                        }
                    }
                });
            }
        });

        textViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(getInfo());
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageView.startUpload(new RCChooseImageView.UpLoadListener() {
                    @Override
                    public void upLoadStart() {
                        button3.setText("图片上传中（点击可以取消上传）");
                        textView.setText(getInfo());
                    }

                    @Override
                    public void upLoadOver(ArrayList<RCPhotoDTO> urlPathList) {
                        button3.setText("上传完成");
                        textView.setText(getInfo());
                    }

                    @Override
                    public void upLoadError() {
                        button3.setText("上传出错");
                    }

                    @Override
                    public void upLoadClose() {
                        button3.setText("上传取消");
                    }
                });
            }
        });
    }


    private String getInfo() {
        List<RCPhotoDTO> rcPhotoDTOS = chooseImageView.getUrlPathList();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; rcPhotoDTOS != null && i < rcPhotoDTOS.size(); i++) {
            buffer.append("序号" + i + ":(" + (rcPhotoDTOS.get(i).isNetwork() ? "网络" : "本地") + "图片)");
            buffer.append("\n\t");
            buffer.append(rcPhotoDTOS.get(i).getPath());
            buffer.append("\n\t");
        }
        return buffer.toString();
    }


}

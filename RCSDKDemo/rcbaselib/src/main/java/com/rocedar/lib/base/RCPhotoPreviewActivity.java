package com.rocedar.lib.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocedar.lib.base.image.photo.RCAlbumActivityUtil;
import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.unit.statuscolor.RCStatusColorHelper;
import com.rocedar.lib.sdk.rcgallery.activity.PreviewFragment;
import com.rocedar.lib.sdk.rcgallery.dto.RCPhotoDTO;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/8 上午9:04
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCPhotoPreviewActivity extends RCBaseActivity {


    public static void goActivity(Context context, String photoInfo, boolean isNetwork, boolean hasChoose) {
        ArrayList<String> photoInfos = new ArrayList<String>();
        photoInfos.add(photoInfo);
        goActivity(context, photoInfos, 0, isNetwork, hasChoose);
    }

    public static void goActivity(Context context, List<String> photoInfo, int chooseIndex, boolean isNetwork, boolean hasChoose) {
        Intent intent = new Intent(context, RCPhotoPreviewActivity.class);
        intent.putExtra("photo_info", (ArrayList<String>) photoInfo);
        intent.putExtra("choose_index", chooseIndex);
        intent.putExtra("isNetwork", isNetwork);
        intent.putExtra("hasChoose", hasChoose);
        context.startActivity(intent);
    }

    public static void goActivity(Context context, List<RCPhotoDTO> photoInfo, int chooseIndex, boolean hasChoose) {
        Intent intent = new Intent(context, RCPhotoPreviewActivity.class);
        intent.putExtra("photo_info_p", (ArrayList<RCPhotoDTO>) photoInfo);
        intent.putExtra("choose_index", chooseIndex);
        intent.putExtra("hasChoose", hasChoose);
        context.startActivity(intent);
    }

    private ImageView back;
    private TextView titleText;

    private void initView() {
        back = findViewById(R.id.rc_activity_photo_preview_back);
        titleText = findViewById(R.id.rc_activity_photo_title_text);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });
    }

    private ArrayList<RCPhotoDTO> list;

    private PreviewFragment fragment;

    private boolean hasChoose = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNotAddHead(true);
        setContentView(R.layout.rc_activity_photo_preview);
        RCStatusColorHelper.statusBarLightMode(mContext, false);
        initView();
        new RCAlbumActivityUtil(mContext).openPreview();
        hasChoose = getIntent().getBooleanExtra("hasChoose", true);
        if (getIntent().hasExtra("photo_info_p")) {
            showContent(R.id.view_framelayout_main, fragment = PreviewFragment.newInstance(
                    list = getIntent().<RCPhotoDTO>getParcelableArrayListExtra("photo_info_p"),
                    getIntent().getIntExtra("choose_index", 0), hasChoose));
        } else {
            if (getIntent().hasExtra("photo_info")) {
                list = new ArrayList<>();
                List<String> infos = getIntent().getStringArrayListExtra("photo_info");
                for (int i = 0; i < infos.size(); i++) {
                    list.add(new RCPhotoDTO(infos.get(i), getIntent().getBooleanExtra("isNetwork", false)));
                }
                showContent(R.id.view_framelayout_main, fragment = PreviewFragment.newInstance(list, 0, hasChoose));
            }
        }
        mRcHeadUtil.setLeftBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });
        if (fragment != null) {
            fragment.setSetListener(new PreviewFragment.OnSetListener() {
                @Override
                public void chooseChange(ArrayList<RCPhotoDTO> list) {

                }
            });
        }

        if (!hasChoose) {
            findViewById(R.id.phont_select_bottm).setVisibility(View.GONE);
        }
        findViewById(R.id.select_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (hasChoose) {
            finish();
            return;
        }
        onBack();
    }

    private void onBack() {
        if (fragment != null) {
            EventBus.getDefault().post(fragment.getChooseList());
        } else {
            EventBus.getDefault().post(list);
        }
        finish();
    }


}

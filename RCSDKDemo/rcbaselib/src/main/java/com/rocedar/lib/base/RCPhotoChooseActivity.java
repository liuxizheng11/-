package com.rocedar.lib.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rocedar.lib.base.image.photo.RCAlbumActivityUtil;
import com.rocedar.lib.base.unit.RCPhotoPreViewUtil;
import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.permission.AcpListener;
import com.rocedar.lib.base.unit.RCPermissionUtil;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.sdk.rcgallery.activity.GalleryPickFragment;
import com.rocedar.lib.sdk.rcgallery.dto.RCPhotoDTO;
import com.rocedar.lib.sdk.rcgallery.inter.IHandlerCallBack;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/4 下午8:46
 * 版本：V1.0.00
 * 描述：瑰柏SDK-图片选择
 * <p>
 *
 * @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 * super.onActivityResult(requestCode, resultCode, data);
 * if (requestCode == this.requestCode) {
 * ArrayList<String> arrayList = data.getStringArrayListExtra
 * (RCChoosePhotoActivity.KEY_CHOOSE_PHOTO_LIST);
 * }
 * }
 * <p>
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCPhotoChooseActivity extends RCBaseActivity {

    public static void goActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, RCPhotoChooseActivity.class);
        intent.putExtra("is_head", true);
        intent.putExtra("max_number", 1);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void goActivity(Context activity, int maxNumber, boolean isHead) {
        Intent intent = new Intent(activity, RCPhotoChooseActivity.class);
        intent.putExtra("is_head", isHead);
        intent.putExtra("max_number", maxNumber);
        intent.putExtra("event_bus", true);
        activity.startActivity(intent);
    }


    public static void goActivity(Activity activity, int maxNumber, int requestCode) {
        Intent intent = new Intent(activity, RCPhotoChooseActivity.class);
        intent.putExtra("is_head", false);
        intent.putExtra("max_number", maxNumber);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void goActivity(Activity activity, boolean isOnlyCamera, int requestCode) {
        Intent intent = new Intent(activity, RCPhotoChooseActivity.class);
        intent.putExtra("is_head", false);
        intent.putExtra("only_camera", isOnlyCamera);
        activity.startActivityForResult(intent, requestCode);
    }

    public static final String KEY_CHOOSE_PHOTO_LIST = "photo_list";


    private GalleryPickFragment galleryPickFragment;

    private IHandlerCallBack iHandlerCallBack;

    //是否仅拍照
    private boolean isOnlyCamera = false;
    //是否剪切
    private boolean isHead = false;
    //最多选择几张图片
    private int maxNumber = 1;
    //选择的图片
    private ArrayList<String> chooseList;

    private RelativeLayout chooseLayout;
    private TextView selectNumber;
    private TextView photoPreview;
    private TextView chooseSubmit;


    private void initView() {
        chooseLayout = findViewById(R.id.activity_choose_photo_bottom_rl);
        selectNumber = findViewById(R.id.photo_select_number);
        photoPreview = findViewById(R.id.photo_preview);
        chooseSubmit = findViewById(R.id.activity_choose_photo_submit);
        chooseSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack(chooseList, true);
            }
        });
        photoPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RCPhotoPreViewUtil(mContext).goPreview(chooseList, false, 0,
                        new RCPhotoPreViewUtil.PreviewListener() {
                            @Override
                            public void over(List<RCPhotoDTO> list) {
                                chooseList.clear();
                                for (int i = 0; i < list.size(); i++) {
                                    chooseList.add(list.get(i).getPath());
                                }
                                if (galleryPickFragment != null) {
                                    galleryPickFragment.setSelectPhoto(chooseList);
                                }
                            }
                        });
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_activity_photo_main);
        initView();
        isHead = getIntent().getBooleanExtra("is_head", false);
        isOnlyCamera = getIntent().getBooleanExtra("only_camera", false);
        maxNumber = getIntent().getIntExtra("max_number", 1);
        if (!isHead && maxNumber > 1) {
            chooseLayout.setVisibility(View.VISIBLE);
            selectNumber.setText("0/" + maxNumber);
        }
        pathName = getString(R.string.gallery_all_folder);
        mRcHeadUtil.setTitle(getText(true), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (galleryPickFragment != null)
                    galleryPickFragment.openFolder(v);
            }
        }).setLeftBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack(new ArrayList<String>(), false);
            }
        });

        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onGallerySuccess(List<String> photoList) {
                onBack((ArrayList<String>) photoList, true);
            }

            @Override
            public void onGalleryError() {
                onBack(null, false);
            }

            @Override
            public void chooseFolder(String name, boolean isOpen) {
                if (name != null)
                    pathName = name;
                mRcHeadUtil.setTitle(getText(!isOpen));
            }

            @Override
            public void chooseChange(List<String> photoList, int maxNumber) {
                chooseList = (ArrayList<String>) photoList;
                selectNumber.setText(photoList.size() + "/" + maxNumber);
            }

        };
        if (isOnlyCamera) {
            new RCAlbumActivityUtil(mContext, iHandlerCallBack).openCamera();
        } else {
            if (isHead) {
                new RCAlbumActivityUtil(mContext, iHandlerCallBack).openGetHeadImage();
            } else {
                new RCAlbumActivityUtil(mContext, iHandlerCallBack).openAblumMore(maxNumber);
            }
        }
        RCPermissionUtil.getPremission(mContext
                , new AcpListener() {
                    @Override
                    public void onGranted() {
                        showContent(R.id.activity_photo_main_layout, galleryPickFragment = GalleryPickFragment.newInstance(false));
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        mContext.finish();
                        RCToast.Center(mContext, "您拒绝了权限，无法选择照片.");
                    }
                }
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.CAMERA);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isHead) {
            new RCAlbumActivityUtil(mContext, iHandlerCallBack).openGetHeadImage();
        } else {
            new RCAlbumActivityUtil(mContext, iHandlerCallBack).openAblumMore(maxNumber);
        }
    }

    private String pathName;

    private String getText(boolean isPackUp) {
        if (isPackUp)
            return pathName + "▼";
        else
            return pathName + "▲";

    }

    /**
     * 回退键监听
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (galleryPickFragment != null && galleryPickFragment.onBack()) {
                return true;
            }
            onBack(null, false);
        }
        return true;
    }


    private void onBack(ArrayList<String> photoList, boolean isOk) {
        if (getIntent().hasExtra("event_bus")
                && getIntent().getBooleanExtra("event_bus", false)) {
            if (isOk) {
                EventBus.getDefault().post(photoList);
            } else {
                EventBus.getDefault().post(new ArrayList<>());
            }
        } else {
            Intent intent = new Intent();
            if (isOk) {
                intent.putStringArrayListExtra(KEY_CHOOSE_PHOTO_LIST, photoList);
                setResult(RESULT_OK, intent);
            } else
                setResult(RESULT_CANCELED, intent);
        }
        finish();
    }


}

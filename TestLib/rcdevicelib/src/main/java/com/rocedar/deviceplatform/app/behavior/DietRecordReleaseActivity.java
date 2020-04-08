package com.rocedar.deviceplatform.app.behavior;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.rocedar.base.RCDateUtil;
import com.rocedar.base.RCDialog;
import com.rocedar.base.RCHandler;
import com.rocedar.base.RCLog;
import com.rocedar.base.RCToast;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.base.network.IResponseData;
import com.rocedar.base.network.RequestData;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.behavior.adapter.DietReleaseImageAdapter;
import com.rocedar.deviceplatform.config.RCDeviceConductID;
import com.rocedar.deviceplatform.config.RCDeviceDeviceID;
import com.rocedar.deviceplatform.config.RCDeviceIndicatorID;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：lxz
 * 日期：17/7/29 下午3:54
 * 版本：V1.0
 * 描述：饮食上传页面
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class DietRecordReleaseActivity extends RCBaseActivity {
    EditText activityDietEditTv;
    RadioButton activityDietBreakFast;
    RadioButton activityDietLunch;
    RadioButton activityDietDinner;
    RadioButton activityDietSnacks;
    ImageView activityDietFriendsIv;
    GridView activityDietPhotoChoose;

    private int status = 1;
    /**
     * 早餐时间
     */
    private final int BreakFastMin = 600;
    private final int BreakFastMax = 900;
    /**
     * 午餐时间
     */
    private final int LunchMin = 1100;
    private final int LunchMax = 1400;
    /**
     * 晚餐时间
     */
    private final int DinnerMin = 1700;
    private final int DinnerMax = 2000;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_record_release);
        ButterKnife.bind(this);
        mRcHeadUtil.setTitle(mContext.getString(R.string.rcdevice_diet));
        mRcHeadUtil.setRightButton(mContext.getString(R.string.rcdevice_record_release), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //必须有图片
                if (urlPathList == null || urlPathList.size() <= 0) {
                    RCToast.Center(mContext, "需要图片");
                    return;
                }
                mRcHandler.sendMessage(RCHandler.START);
                mRcHandler.setOutTime(3 * 60 * 1000);
                submitImage(0);
            }
        });
        initView();
        initGridViewView();
    }

    private void initView() {
        activityDietEditTv = (EditText) findViewById(R.id.activity_diet_editTv);
        activityDietBreakFast = (RadioButton) findViewById(R.id.activity_diet_break_fast);
        activityDietLunch = (RadioButton) findViewById(R.id.activity_diet_lunch);
        activityDietDinner = (RadioButton) findViewById(R.id.activity_diet_dinner);
        activityDietSnacks = (RadioButton) findViewById(R.id.activity_diet_snacks);
        activityDietFriendsIv = (ImageView) findViewById(R.id.activity_diet_friends_iv);
        activityDietPhotoChoose = (GridView) findViewById(R.id.activity_diet_photo_choose);

        activityDietFriendsIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == 1) {
                    status = 0;
                    activityDietFriendsIv.setImageResource(R.mipmap.ic_diet_friend);
                } else {
                    status = 1;
                    activityDietFriendsIv.setImageResource(R.mipmap.ic_diet_friend_select);
                }
            }
        });

        /**判断当前时间 */
        final int time = Integer.parseInt(RCDateUtil.getFormatNow("Hmm"));
        if (time > BreakFastMin && time < BreakFastMax) {
            activityDietBreakFast.setChecked(true);
        } else if (time > LunchMin && time < LunchMax) {
            activityDietLunch.setChecked(true);
        } else if (time > DinnerMin && time < DinnerMax) {
            activityDietDinner.setChecked(true);
        } else {
            activityDietSnacks.setChecked(true);
        }

        activityDietBreakFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (time > BreakFastMin && time < BreakFastMax) {
                    activityDietBreakFast.setChecked(true);
                    activityDietSnacks.setChecked(false);
                } else {
                    activityDietBreakFast.setChecked(false);
                }
            }
        });
        activityDietLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (time > LunchMin && time < LunchMax) {
                    activityDietLunch.setChecked(true);
                    activityDietSnacks.setChecked(false);
                } else {
                    activityDietLunch.setChecked(false);
                }
            }
        });
        activityDietDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (time > DinnerMin && time < DinnerMax) {
                    activityDietDinner.setChecked(true);
                    activityDietSnacks.setChecked(false);
                } else {
                    activityDietDinner.setChecked(false);
                }
            }
        });
        activityDietSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityDietSnacks.setChecked(true);
                activityDietBreakFast.setChecked(false);
                activityDietLunch.setChecked(false);
                activityDietDinner.setChecked(false);
            }
        });

    }

    private void postData() {
//        DietRecordReleaseBean mBean = new DietRecordReleaseBean();
//        mBean.setActionName("/hy/diet/");
//        mBean.setDevice_id(RCDeviceDeviceID.Phone + "");
//        mBean.setImages(imageNetPath);
//        mBean.setConduct_id(RCDeviceConductID.DIET + "");
//        if (activityDietBreakFast.isChecked()) {
//            //早餐
//            mBean.setIndicator_id(RCDeviceIndicatorID.BreakFast + "");
//        }
//        if (activityDietLunch.isChecked()) {
//            //午餐
//            mBean.setIndicator_id(RCDeviceIndicatorID.Lunch + "");
//        }
//        if (activityDietDinner.isChecked()) {
//            //晚餐
//            mBean.setIndicator_id(RCDeviceIndicatorID.Dinner + "");
//        }
//        if (activityDietSnacks.isChecked()) {
//            //加餐
//            mBean.setIndicator_id(RCDeviceIndicatorID.Snacks + "");
//        }
//        mBean.setIs_public(status + "");
//        mBean.setMessage(activityDietEditTv.getText().toString().trim());
//        RequestData.NetWorkGetData(mContext, mBean, RequestData.Method.Post,
//                new IResponseData() {
//                    @Override
//                    public void getDataSucceedListener(JSONObject data) {
//                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
//                        finishActivity();
//                    }
//
//                    @Override
//                    public void getDataErrorListener(String msg, int status) {
//                        mRcHandler.sendMessage(RCHandler.GETDATA_OK);
//                    }
//                });
//    }

    }

    private DietReleaseImageAdapter mAdapter;

    private static final int REQUEST_CODE_PHOTO = 1001;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 1002;

    //选择的图片地址
    private ArrayList<String> urlPathList = new ArrayList<>();

    private void initGridViewView() {
        activityDietPhotoChoose.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new DietReleaseImageAdapter(this, urlPathList);
        activityDietPhotoChoose.setAdapter(mAdapter);
        activityDietPhotoChoose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                if (position == getDataSize()) {//点击弹出选项框,打开相机
//                    PhotoManipulation.goActivity(mContext,
//                            9 - getDataSize(), REQUEST_CODE_PHOTO);
//                } else {//点进查看照片的viewpager
//                    PhotoLocationPreviewActivity.goActivityPhotoPreview(mContext,
//                            urlPathList, REQUEST_CODE_PHOTO_PREVIEW, position);
//                }
            }
        });
    }

    private int getDataSize() {
        return urlPathList == null ? 0 : urlPathList.size();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PHOTO:
                    if (data.hasExtra("list")) {
//                        ArrayList<String> temp = data
//                                .getStringArrayListExtra("list");
//                        if (null != temp) {
//                            for (int i = 0; i < temp.size(); i++) {
//                                String mUrl = temp.get(i);
//                                File mFile = new File(mUrl);
//                                int degree = FZBitmap.readPictureDegree(mFile.getAbsolutePath());
//                                if (degree != 0) {
//                                    Bitmap bm = FZBitmap.decodeFile(mFile, 100);
//                                    Bitmap bitmap = FZBitmap.rotaingImageView(degree, bm);
//                                    File mFileRotated = FZBitmap.compressHeadPhoto(bitmap, System
//                                            .currentTimeMillis());
//                                    String mAbsolutePath = mFileRotated.getAbsolutePath();
//                                    urlPathList.add(mAbsolutePath);
//                                } else {
//                                    urlPathList.add(mUrl);
//                                }
//                            }
//                        }
                        mAdapter = new DietReleaseImageAdapter(this, urlPathList);
                        activityDietPhotoChoose.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case REQUEST_CODE_PHOTO_PREVIEW:
                    if (data.hasExtra("choose_list")) {
                        urlPathList = data.getStringArrayListExtra("choose_list");
                        mAdapter = new DietReleaseImageAdapter(this, urlPathList);
                        activityDietPhotoChoose.setAdapter(mAdapter);
                    }
                    break;
            }
        }
    }

    //是否正在提交
    private boolean OnLoadSubmit = true;
    //用于保存时的图片地址
    private String imageNetPath = "";
    //上传后图片的网络地址
    private List<String> imageNetPathList = new ArrayList<String>();

    public void submitImage(final int index) {
//        mRcHandler.sendMessage(RCHandler.MESSAGE, getString(R.string.rcdevice_record_upload_image) +
//                "(" + index + "/"
//                + urlPathList.size() + ")");
//        if (index == 0) {
//            OnLoadSubmit = true;
//            imageNetPath = "";
//            imageNetPathList = new ArrayList<>();
//            mRcHandler.setmDismissListener(new RCDialog.DialogDismissListener() {
//
//                @Override
//                public void onDismiss() {
//                    RCLog.i("取消发送请求-上传图片");
//                    OnLoadSubmit = false;
//                    mRcHandler.sendMessage(RCHandler.GETDATA_OK);
//                }
//            });
//        }
//        new UploadImage(new UploadImage.UploadListener() {
//
//            @Override
//            public void onProgressListener(int p) {
//                mRcHandler.sendMessage(RCHandler.MESSAGE, getString(R.string.rcdevice_record_upload_image) +
//                        "(" + (index + 1)
//                        + "/" + urlPathList.size() + ")");
//
//            }
//
//            @Override
//            public void onUpLoadOverOk(String url) {
//                if (!OnLoadSubmit) {
//                    return;
//                }
//                imageNetPathList.add(url);
//                if (index == urlPathList.size() - 1) {
//                    imageNetPath += url;
//                    postData();
//                } else {
//                    imageNetPath += url + ",";
//                    submitImage(index + 1);
//                }
//            }
//
//            @Override
//            public void onUpLoadOverError() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        RCToast
//                                .Center(mContext, getString(R.string.rcdevice_record_upload_image_error), false);
//                    }
//                });
//                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
//            }
//        }, new File(urlPathList.get(index)), UploadImage.Models.UserCircle);
    }


}

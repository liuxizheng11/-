package com.rocedar.deviceplatform.app.behavior;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocedar.base.RCDateUtil;
import com.rocedar.base.RCHandler;
import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.behavior.adapter.DietRecordParticularsAdapter;
import com.rocedar.deviceplatform.app.behavior.dto.BehaviorLibraryDietDTO;
import com.rocedar.deviceplatform.app.view.MyListView;
import com.rocedar.deviceplatform.config.RCDeviceConductID;
import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorLibraryDTO;
import com.rocedar.deviceplatform.request.impl.RCBehaviorLibraryImpl;
import com.rocedar.deviceplatform.request.listener.behaviorlibrary.RCBehaviorLibraryListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：lxz
 * 日期：17/7/28 下午5:50
 * 版本：V1.0
 * 描述：饮食记录详情页
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class DietRecordParticularsActivity extends RCBaseActivity {
    TextView dietRecordAllNumber;
    TextView dietRecordConsumeNumber;
    MyListView dietRecordParticularsListview;
    TextView dietAddFoodClick;
    ImageView dietRecordParticularsDontHaveDataIv;

    private DietRecordParticularsAdapter mAdapter;
    private RCBehaviorLibraryImpl behaviorLibrary;

    private List<BehaviorLibraryDietDTO> mList = new ArrayList<>();
    /**
     * 数据时间 (档案页面传入)
     */
    private long data_time = -1;

    public static void goActivity(Context context, long data_time) {
        Intent intent = new Intent(context, DietRecordParticularsActivity.class);
        intent.putExtra("data_time", data_time);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_record_particulars);
        ButterKnife.bind(this);
        mRcHeadUtil.setTitle(mContext.getString(R.string.rcdevice_diet));
        behaviorLibrary = RCBehaviorLibraryImpl.getInstance(mContext);
        data_time = getIntent().getLongExtra("data_time", -1);
        initView();


    }

    private void initView() {
        dietRecordAllNumber = (TextView) findViewById(R.id.diet_record_all_number);
        dietRecordConsumeNumber = (TextView) findViewById(R.id.diet_record_consume_number);
        dietRecordParticularsListview = (MyListView) findViewById(R.id.diet_record_particulars_listview);
        dietAddFoodClick = (TextView) findViewById(R.id.diet_add_food_click);
        dietRecordParticularsDontHaveDataIv = (ImageView) findViewById(R.id.diet_record_particulars_dont_have_data_iv);

        mAdapter = new DietRecordParticularsAdapter(mContext, mList);
        dietRecordParticularsListview.setAdapter(mAdapter);

        dietRecordParticularsListview.setFocusable(false);

        /**判断是否 是当天*/
        if (data_time > 0) {
            if (Integer.parseInt(RCDateUtil.formatServiceTime(data_time + "", "yyyyMMdd")) !=
                    Integer.parseInt(RCDateUtil.getFormatNow("yyyyMMdd"))) {
                dietAddFoodClick.setVisibility(View.GONE);
            }
        }
        /**设置字体格式*/
//        FZFontCustom.getSlenderFont(mContext, dietRecordAllNumber);
        mAdapter = new DietRecordParticularsAdapter(mContext, mList);
        dietRecordParticularsListview.setAdapter(mAdapter);
        /**添加饮食记录*/
        dietAddFoodClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, DietRecordReleaseActivity.class));
            }
        });
    }

    private void initData() {
        mRcHandler.sendMessage(RCHandler.START);
        if (data_time > 0) {
            /**档案详情页请求*/
            behaviorLibrary.getBehaviorListDetailData("", RCDeviceConductID.DIET + "", data_time + "",
                    new RCBehaviorLibraryListener() {
                        @Override
                        public void getDataSuccess(RCBehaviorLibraryDTO dtoList) {
                            mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                            dietRecordAllNumber.setText(dtoList.getNeed() + "");
                            dietRecordConsumeNumber.setText("运动已消耗 - " + dtoList.getYet() + "kcal");
                            if (dtoList.getDietList().size() > 0) {
                                dietRecordParticularsDontHaveDataIv.setVisibility(View.GONE);
                                dietRecordParticularsListview.setVisibility(View.VISIBLE);

                                for (int i = 0; i < dtoList.getDietList().size(); i++) {
                                    BehaviorLibraryDietDTO mDTO = new BehaviorLibraryDietDTO();

                                    mDTO.setUser_id(dtoList.getDietList().get(i).getUser_id());
                                    mDTO.setConduct_id(dtoList.getDietList().get(i).getConduct_id());
                                    mDTO.setIndicator_id(dtoList.getDietList().get(i).getIndicator_id());
                                    mDTO.setDevice_id(dtoList.getDietList().get(i).getDevice_id());
                                    mDTO.setData_time(dtoList.getDietList().get(i).getData_time());
                                    mDTO.setDiet_images(dtoList.getDietList().get(i).getDiet_images());
                                    mDTO.setDiet_message(dtoList.getDietList().get(i).getDiet_message());
                                    mDTO.setUpdate_time(dtoList.getDietList().get(i).getUpdate_time());
                                    mDTO.setNeed(dtoList.getDietList().get(i).getNeed());
                                    mDTO.setYet(dtoList.getDietList().get(i).getYet());
                                    mList.add(mDTO);
                                }

                            } else {
                                //空页面
                                if (mList.size() <= 0) {
                                    dietRecordParticularsDontHaveDataIv.setVisibility(View.VISIBLE);
                                    dietRecordParticularsListview.setVisibility(View.GONE);
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void getDataError(int status, String msg) {
                            mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                        }
                    });
        } else {
            /**行为库请求*/
            behaviorLibrary.getBehaviorListDetailData("", RCDeviceConductID.DIET + "", RCDateUtil.getFormatNow("yyyyMMdd"), new RCBehaviorLibraryListener() {
                @Override
                public void getDataSuccess(RCBehaviorLibraryDTO dtoList) {
                    mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                    dietRecordAllNumber.setText(dtoList.getNeed() + "");
                    dietRecordConsumeNumber.setText("运动已消耗 - " + dtoList.getYet() + "kcal");
                    if (dtoList.getDietList().size() > 0) {
                        dietRecordParticularsDontHaveDataIv.setVisibility(View.GONE);
                        dietRecordParticularsListview.setVisibility(View.VISIBLE);

                        for (int i = 0; i < dtoList.getDietList().size(); i++) {
                            BehaviorLibraryDietDTO mDTO = new BehaviorLibraryDietDTO();

                            mDTO.setUser_id(dtoList.getDietList().get(i).getUser_id());
                            mDTO.setConduct_id(dtoList.getDietList().get(i).getConduct_id());
                            mDTO.setIndicator_id(dtoList.getDietList().get(i).getIndicator_id());
                            mDTO.setDevice_id(dtoList.getDietList().get(i).getDevice_id());
                            mDTO.setData_time(dtoList.getDietList().get(i).getData_time());
                            mDTO.setDiet_images(dtoList.getDietList().get(i).getDiet_images());
                            mDTO.setDiet_message(dtoList.getDietList().get(i).getDiet_message());
                            mDTO.setUpdate_time(dtoList.getDietList().get(i).getUpdate_time());
                            mDTO.setNeed(dtoList.getDietList().get(i).getNeed());
                            mDTO.setYet(dtoList.getDietList().get(i).getYet());
                            mList.add(mDTO);
                        }
                    } else {
                        //空页面
                        if (mList.size() <= 0) {
                            dietRecordParticularsDontHaveDataIv.setVisibility(View.VISIBLE);
                            dietRecordParticularsListview.setVisibility(View.GONE);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }


                @Override
                public void getDataError(int status, String msg) {
                    mRcHandler.sendMessage(RCHandler.GETDATA_OK);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mList.clear();
        initData();
    }
}

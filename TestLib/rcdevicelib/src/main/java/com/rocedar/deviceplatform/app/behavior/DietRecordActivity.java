package com.rocedar.deviceplatform.app.behavior;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rocedar.base.manger.RCBaseActivity;
import com.rocedar.base.view.PullOnLoading;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.behavior.adapter.DietRecordAdapter;
import com.rocedar.deviceplatform.app.view.MyListView;
import com.rocedar.deviceplatform.config.RCDeviceConductID;
import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorLibraryDTO;
import com.rocedar.deviceplatform.dto.behaviorlibrary.RCBehaviorLibraryDietDTO;
import com.rocedar.deviceplatform.request.impl.RCBehaviorLibraryImpl;
import com.rocedar.deviceplatform.request.listener.behaviorlibrary.RCBehaviorLibraryListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 作者：lxz
 * 日期：17/7/27 下午9:04
 * 版本：V1.0
 * 描述：饮食记录页面
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class DietRecordActivity extends RCBaseActivity {
    TextView dietRecordAllNumber;
    TextView dietRecordConsumeNumber;
    MyListView dietRecordListview;
    ScrollView dietRecordScroll;
    ImageView dontHaveDataLl;

    /**
     * 获取数据 分页单页最大条数
     */
    public static int maxPNData = 20;
    private DietRecordAdapter mAdapter;

    private RCBehaviorLibraryImpl behaviorLibrary;

    private List<RCBehaviorLibraryDietDTO> mList = new ArrayList();
    private int pn = 0;
    private PullOnLoading pullOnLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_record_main);
        ButterKnife.bind(this);

        mRcHeadUtil.setTitle(mContext.getString(R.string.rcdevice_diet_record));
        behaviorLibrary = RCBehaviorLibraryImpl.getInstance(mContext);
        pullOnLoading = new PullOnLoading(mContext, dietRecordScroll);
        pullOnLoading.setOnPullOnLoadingLintener(new PullOnLoading.OnPullOnLoadingLintener() {
            @Override
            public void loading() {
                initData();
            }
        });
        initView();
    }

    private void initData() {
        behaviorLibrary.getHealthRecordBehaviorData("", pn + "", RCDeviceConductID.DIET + "",
                new RCBehaviorLibraryListener() {
                    @Override
                    public void getDataSuccess(RCBehaviorLibraryDTO dtoList) {
                        dietRecordAllNumber.setText(dtoList.getNeed() + "");
                        dietRecordConsumeNumber.setText("运动已消耗 - " + dtoList.getYet() + "kcal");
                        if (dtoList.getDietList().size() > 0) {
                            dontHaveDataLl.setVisibility(View.GONE);
                            dietRecordListview.setVisibility(View.VISIBLE);
                            mList = dtoList.getDietList();
                        } else {
                            //空页面
                            if (mList.size() <= 0) {
                                dontHaveDataLl.setVisibility(View.VISIBLE);
                                dietRecordListview.setVisibility(View.GONE);
                            }
                        }
                        pn++;
                        pullOnLoading.loadOver(dtoList.getDietList().size() >= maxPNData);
                        mAdapter = new DietRecordAdapter(mContext, mList);
                        dietRecordListview.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void getDataError(int status, String msg) {

                    }
                });
    }

    private void initView() {
        dietRecordAllNumber = (TextView) findViewById(R.id.diet_record_all_number);
        dietRecordConsumeNumber = (TextView) findViewById(R.id.diet_record_consume_number);
        dietRecordListview = (MyListView) findViewById(R.id.diet_record_listview);
        dietRecordScroll = (ScrollView) findViewById(R.id.diet_record_scroll);
        dontHaveDataLl = (ImageView) findViewById(R.id.diet_record_dont_have_data_iv);

        dietRecordListview.setFocusable(false);

        dietRecordListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                DietRecordParticularsActivity.goActivity(mContext, Long.parseLong(mList.get(position).getData_time()));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mList.clear();
        pn = 0;
        initData();
    }
}

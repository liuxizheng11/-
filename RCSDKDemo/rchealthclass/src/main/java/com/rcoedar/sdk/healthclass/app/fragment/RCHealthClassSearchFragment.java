package com.rcoedar.sdk.healthclass.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rcoedar.sdk.healthclass.R;
import com.rcoedar.sdk.healthclass.app.RCHealthClassVideoActivity;
import com.rcoedar.sdk.healthclass.app.adapter.RCHealthClassSearchAdapter;
import com.rcoedar.sdk.healthclass.dto.RCHealthClassSearchDTO;
import com.rcoedar.sdk.healthclass.request.impl.RCHealthClassmpl;
import com.rcoedar.sdk.healthclass.request.listener.RCGetHealthCLassSearchListener;
import com.rocedar.lib.base.manage.RCBaseActivity;
import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCTPJump;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/9/12 下午4:56
 * 版本：V1.0
 * 描述：健康课堂 搜索页面
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCHealthClassSearchFragment extends RCBaseFragment {
    private LinearLayout rc_fragment_health_calss_search_ll;
    private EditText rc_fragment_health_calss_search_et;
    private TextView rc_fragment_health_calss_search_cancle;
    private ListView rc_fragment_health_calss_search_list;
    private TextView rc_fragment_health_calss_search_nodata;

    private RCHealthClassmpl rcHealthClassmpl;
    private RCHealthClassSearchAdapter classSearchAdapter;
    private List<RCHealthClassSearchDTO> searchDTOList = new ArrayList<>();

    public static RCHealthClassSearchFragment newInstance() {
        RCHealthClassSearchFragment fragment = new RCHealthClassSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rc_fragment_health_class_search, null);
        rcHealthClassmpl = RCHealthClassmpl.getInstance(mActivity);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rc_fragment_health_calss_search_ll = view.findViewById(R.id.rc_fragment_health_calss_search_ll);
        rc_fragment_health_calss_search_et = view.findViewById(R.id.rc_fragment_health_calss_search_et);
        rc_fragment_health_calss_search_cancle = view.findViewById(R.id.rc_fragment_health_calss_search_cancle);
        rc_fragment_health_calss_search_list = view.findViewById(R.id.rc_fragment_health_calss_search_list);
        rc_fragment_health_calss_search_nodata = view.findViewById(R.id.rc_fragment_health_calss_search_nodata);

        rc_fragment_health_calss_search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getSearchData(editable.toString());
            }
        });

        rc_fragment_health_calss_search_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });
        rc_fragment_health_calss_search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (searchDTOList.get(i).getVideo() == 1) {
                    RCHealthClassVideoActivity.goActivity(mActivity, searchDTOList.get(i).getInfo_id());
                } else {
                    RCTPJump.ActivityJump(mActivity, searchDTOList.get(i).getInfo_url());
                }
            }
        });
    }

    private void getSearchData(String title) {
        rcHealthClassmpl.getHealthSearchData(title, new RCGetHealthCLassSearchListener() {
            @Override
            public void getDataSuccess(List<RCHealthClassSearchDTO> list) {
                if (list != null && list.size() > 0) {
                    searchDTOList = list;
                    rc_fragment_health_calss_search_list.setVisibility(View.VISIBLE);
                    rc_fragment_health_calss_search_nodata.setVisibility(View.GONE);
                    searchDTOList = list;
                    classSearchAdapter = new RCHealthClassSearchAdapter(mActivity, list);
                    rc_fragment_health_calss_search_list.setAdapter(classSearchAdapter);
                    classSearchAdapter.notifyDataSetChanged();
                } else {
                    rc_fragment_health_calss_search_list.setVisibility(View.GONE);
                    rc_fragment_health_calss_search_nodata.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void getDataError(int i, String s) {
            }
        });

    }
}

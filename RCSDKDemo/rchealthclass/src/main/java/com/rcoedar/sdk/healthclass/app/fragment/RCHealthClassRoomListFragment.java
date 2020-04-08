package com.rcoedar.sdk.healthclass.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.rcoedar.sdk.healthclass.R;
import com.rcoedar.sdk.healthclass.app.RCHealthClassVideoActivity;
import com.rcoedar.sdk.healthclass.app.adapter.RCHealthyClassListAdapter;
import com.rcoedar.sdk.healthclass.dto.RCHealthClassroomDTO;
import com.rcoedar.sdk.healthclass.request.impl.RCHealthClassmpl;
import com.rcoedar.sdk.healthclass.request.listener.RCGetHealthCLassListListener;
import com.rocedar.lib.base.manage.RCBaseFragment;
import com.rocedar.lib.base.network.IResponseData;
import com.rocedar.lib.base.network.RCRequestNetwork;
import com.rocedar.lib.base.unit.RCHandler;
import com.rocedar.lib.base.unit.RCTPJump;
import com.rocedar.lib.base.view.loading.PullOnLoading;

import java.util.ArrayList;
import java.util.List;


/**
 * @desc 健康课堂列表
 * @veison V3600
 */

public class RCHealthClassRoomListFragment extends RCBaseFragment {
    GridView lvHealthClassroom;
    private View view;
    private int pn = 0;
    private int type_id;
    private PullOnLoading pullOnLoading;
    private List<RCHealthClassroomDTO> mHealthInfos = new ArrayList<>();
    private RCHealthyClassListAdapter healthyTaskListAdapter;
    private RCHealthClassmpl rcHealthClassmpl;

    public static RCHealthClassRoomListFragment newInstance(int type_id) {
        RCHealthClassRoomListFragment fragment = new RCHealthClassRoomListFragment();
        Bundle args = new Bundle();
        args.putInt("type_id", type_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }

        view = inflater.inflate(R.layout.rc_activity_health_class_list, null);
        lvHealthClassroom = view.findViewById(R.id.rc_activity_health_listview);
        type_id = getArguments().getInt("type_id", 0);
        rcHealthClassmpl = RCHealthClassmpl.getInstance(mActivity);

        healthyTaskListAdapter = new RCHealthyClassListAdapter(mHealthInfos, mActivity);
        lvHealthClassroom.setAdapter(healthyTaskListAdapter);

        loadData();

        pullOnLoading = new PullOnLoading(mActivity, view,lvHealthClassroom);
        pullOnLoading.setOnPullOnLoadingLintener(new PullOnLoading.OnPullOnLoadingListener() {
            @Override
            public void loading() {
                loadData();
            }
        });
        lvHealthClassroom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //0,图文；1,视频
                if (mHealthInfos.get(position).getVideo() == 1) {
                    RCHealthClassVideoActivity.goActivity(mActivity, mHealthInfos.get(position).getInfo_id());
                } else {
                    RCTPJump.ActivityJump(mActivity, mHealthInfos.get(position).getInfo_url());
                }
            }
        });
        return view;
    }


    private void loadData() {
        mRcHandler.sendMessage((RCHandler.START));
        rcHealthClassmpl.getHealthInfoListData(pn, type_id, new RCGetHealthCLassListListener() {
            @Override
            public void getDataSuccess(List<RCHealthClassroomDTO> list) {
                mHealthInfos.addAll(list);
                for (int i = 0; i < mHealthInfos.size(); i++) {
                    if (mHealthInfos.get(i).getVideo() == 1) {
                        lvHealthClassroom.setNumColumns(2);
                    }else{
                        lvHealthClassroom.setNumColumns(1);
                    }
                }


                healthyTaskListAdapter.notifyDataSetChanged();
                pullOnLoading.loadOver(list.size() == 20);
                pn++;
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }

            @Override
            public void getDataError(int status, String msg) {
                mRcHandler.sendMessage(RCHandler.GETDATA_OK);
            }
        });

    }
}

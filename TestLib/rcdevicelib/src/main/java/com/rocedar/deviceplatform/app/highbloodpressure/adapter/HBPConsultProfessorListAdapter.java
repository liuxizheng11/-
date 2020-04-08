package com.rocedar.deviceplatform.app.highbloodpressure.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocedar.base.RCDisplayUtil;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.app.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;



/**
 * @author liuyi
 * @date 2017/11/24
 * @desc
 * @veison
 */

public class HBPConsultProfessorListAdapter extends RecyclerView.Adapter {


    private List<String> mDatas;
    private Context mContext;

    public HBPConsultProfessorListAdapter(Context mContext, List<String> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_SYSTEM:
                return new SystemHolder(getView(parent, R.layout.item_list_hbp_consult_left));
            case TYPE_USER:
                return new UserHolder(getView(parent, R.layout.item_list_hbp_consult_right));
            case TYPE_CONSULT:
                return new ConsultHolder(getView(parent, R.layout.item_list_hbp_consult_user));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SystemHolder) {
            doTypeSystem((SystemHolder) holder, mDatas.get(position));
        } else if (holder instanceof UserHolder) {
            doTypeUser((UserHolder) holder, mDatas.get(position));
        } else if (holder instanceof ConsultHolder) {
            ConsultHolder consultHolder = (ConsultHolder) holder;
            consultHolder.list.add(0, "自己");
            consultHolder.list.add("我们");
            consultHolder.list.add("你们");
            consultHolder.list.add("他人");
            consultHolder.adapter.notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 为谁咨询
     */
    public static final int TYPE_CONSULT = 0xff11;
    /**
     * 系统消息
     */
    public static final int TYPE_SYSTEM = 0xff12;
    /**
     * 用户消息
     */
    public static final int TYPE_USER = 0xff13;

    @Override
    public int getItemViewType(int position) {
        if (Integer.parseInt(mDatas.get(position)) % 3 == 0) {
            return TYPE_USER;
        }

        if (Integer.parseInt(mDatas.get(position)) % 5 == 0) {
            return TYPE_CONSULT;
        }

        return TYPE_SYSTEM;
    }

    private void doTypeSystem(SystemHolder holder, String s) {

    }

    private void doTypeUser(UserHolder holder, String s) {

    }

    private View getView(ViewGroup parent, int layout) {
        return LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
    }


    public class ConsultHolder extends RecyclerView.ViewHolder {

        RecyclerView rvItemHbpConsultUser;
        TextView tvItemHbpConsultUserPerfect;
        private HBPChooseUserGridAdapter adapter;
        private List<String> list = new ArrayList<>();

        public ConsultHolder(View itemView) {
            super(itemView);
            rvItemHbpConsultUser = (RecyclerView) itemView.findViewById(R.id.rv_item_hbp_consult_user);
            tvItemHbpConsultUserPerfect = (TextView) itemView.findViewById(R.id.tv_item_hbp_consult_user_perfect);
            rvItemHbpConsultUser.setLayoutManager(new GridLayoutManager(mContext, 3));
            rvItemHbpConsultUser.addItemDecoration(new SpaceItemDecoration(3, RCDisplayUtil.dip2px(mContext,10),RCDisplayUtil.dip2px(mContext,10)));
            rvItemHbpConsultUser.setAdapter(adapter = new HBPChooseUserGridAdapter(list, mContext));
        }
    }

    public class SystemHolder extends RecyclerView.ViewHolder {
        TextView ivItemHbpConsultTimeLeft;
        ImageView ivItemHbpConsultHeadLeft;
        ImageView ivItemHbpConsultHeadLeftNew;
        TextView tvItemHbpConsultContentLeft;

        public SystemHolder(View itemView) {
            super(itemView);
            ivItemHbpConsultTimeLeft = (TextView) itemView.findViewById(R.id.iv_item_hbp_consult_time_left);
            ivItemHbpConsultHeadLeft = (ImageView) itemView.findViewById(R.id.iv_item_hbp_consult_head_left);
            ivItemHbpConsultHeadLeftNew = (ImageView) itemView.findViewById(R.id.iv_item_hbp_consult_head_left_new);
            tvItemHbpConsultContentLeft = (TextView) itemView.findViewById(R.id.tv_item_hbp_consult_content_left);

        }
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        TextView tvItemHbpConsultTimeRight;
        CircleImageView civItemHbpConsultHeadRight;
        TextView tvItemHbpConsultContentRight;

        public UserHolder(View itemView) {
            super(itemView);
            tvItemHbpConsultTimeRight = (TextView) itemView.findViewById(R.id.tv_item_hbp_consult_time_right);
            civItemHbpConsultHeadRight = (CircleImageView) itemView.findViewById(R.id.civ_item_hbp_consult_head_right);
            tvItemHbpConsultContentRight = (TextView) itemView.findViewById(R.id.tv_item_hbp_consult_content_right);

        }
    }
}

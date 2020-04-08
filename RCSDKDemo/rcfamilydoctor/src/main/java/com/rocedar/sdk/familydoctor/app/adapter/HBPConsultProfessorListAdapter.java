package com.rocedar.sdk.familydoctor.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocedar.lib.base.RCPhotoPreviewActivity;
import com.rocedar.lib.base.unit.RCAndroid;
import com.rocedar.lib.base.unit.RCDateUtil;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.base.unit.RCToast;
import com.rocedar.lib.base.view.CircleImageView;
import com.rocedar.sdk.familydoctor.R;
import com.rocedar.sdk.familydoctor.app.RCFDAddUserActivity;
import com.rocedar.sdk.familydoctor.app.RCFDHBPConsultProfessorActivity;
import com.rocedar.sdk.familydoctor.app.util.SpaceItemDecoration;
import com.rocedar.sdk.familydoctor.dto.hdp.RCHBPRecordListDTO;
import com.rocedar.sdk.familydoctor.request.IRCFDHBPRequest;
import com.rocedar.sdk.familydoctor.request.impl.RCFDHBPRequestImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * @author liuyi
 * @date 2017/11/24
 * @desc
 * @veison
 */

public class HBPConsultProfessorListAdapter extends RecyclerView.Adapter {


    private IRCFDHBPRequest hbpRequest;
    private List<RCHBPRecordListDTO> mDatas;
    private RCFDHBPConsultProfessorActivity mContext;
    private int orgId;

    public HBPConsultProfessorListAdapter(RCFDHBPConsultProfessorActivity mContext,
                                          List<RCHBPRecordListDTO> mDatas, int orgId) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.orgId = orgId;
        hbpRequest = new RCFDHBPRequestImpl(mContext);
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
            case TYPE_PHOTO:
                return new PhotoHolder(getView(parent, R.layout.item_list_hbp_consult_photo));
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
            doTypeConsult((ConsultHolder) holder, mDatas.get(position).getSicks(), position);
        } else if (holder instanceof PhotoHolder) {
            doTypePhoto((PhotoHolder) holder, mDatas.get(position));
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
    /**
     * 用户消息
     */
    public static final int TYPE_PHOTO = 0xff14;

    @Override
    public int getItemViewType(int position) {

        int type = mDatas.get(position).getType();
        int speaker = mDatas.get(position).getSpeaker();
        if (type == 1 && speaker == 1) {
            return TYPE_USER;
        }

        if (type == 0 && speaker == 0) {
            return TYPE_CONSULT;
        }

        if (type == 2 && speaker == 1) {
            return TYPE_PHOTO;
        }

        if ((type == 1 && speaker == 0) || (type == 1 && speaker == 2)) {
            return TYPE_SYSTEM;
        }

        return 0;
    }

    private void doTypeConsult(final ConsultHolder holder, final List<RCHBPRecordListDTO.SicksDTO> list,
                               final int index) {
        holder.tvItemHbpConsultUserPerfect.setEnabled(false);
        if (orgId == 1001) {
            holder.iv_item_hbp_consult_user_head.setImageResource(R.mipmap.rc_fd_ic_head_hbp);
        } else {
            holder.iv_item_hbp_consult_user_head.setImageResource(R.mipmap.rc_fd_ic_head_hdy);
        }

        holder.tvItemHbpConsultUserPerfect.setText("去完善");
        holder.tvItemHbpConsultUserPerfect.setTextColor(RCAndroid.getAttColor(mContext, R.attr.RCDarkColor));
        holder.tvItemHbpConsultUserPerfect.setEnabled(true);

        //显示问卷是否已填写
        boolean isPrefect = false;
        List<RCHBPRecordListDTO.SicksDTO> sicks = mDatas.get(index).getSicks();
        choose:
        for (int i = 0; i < sicks.size(); i++) {
            if (sicks.get(i).getChoose() == 1) {//已选择
                isPrefect = true;
                if (sicks.get(i).getQuestionnaire_id() > 0) {
                    holder.tvItemHbpConsultUserPerfect.setText("已完善");
                    holder.tvItemHbpConsultUserPerfect.setTextColor(Color.parseColor("#666666"));
                    holder.tvItemHbpConsultUserPerfect.setEnabled(false);
                }
                break choose;
            }
        }

        final boolean finalIsPrefect = isPrefect;
        holder.adapter.setOnItemClickListener(new HBPChooseUserGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                if (finalIsPrefect)
                    return;

                final int position = (int) view.getTag();
                RCHBPRecordListDTO.SicksDTO dto = list.get(position);
                if (dto.getSick_id() == -1) {//选择其他的时候填写资料
                    gotoEdit(mContext, new SaveInfoListener() {
                        @Override
                        public void over(boolean isSuccess, String name, long userId) {
                            if (isSuccess) {
                                RCHBPRecordListDTO.SicksDTO sicksDTO = new RCHBPRecordListDTO.SicksDTO();
                                sicksDTO.setSick_id(userId);
                                sicksDTO.setSick_name(name);
                                sicksDTO.setChoose(2);
                                holder.adapter.addUser(sicksDTO);
                                holder.adapter.setSelectUser(sicksDTO.getSick_id());
                                ArrayList<RCHBPRecordListDTO.SicksDTO> temp = mDatas.get(index).getSicks();
                                temp.add(temp.size() - 1, sicksDTO);
                                mDatas.get(index).setSicks(temp);
                                notifyItemChanged(index);
                            }
                        }
                    });
                } else {
                    holder.adapter.setSelectUser(dto.getSick_id());
                }

                if (dto.getQuestionnaire_id() < 0) {//未填问卷
                    holder.tvItemHbpConsultUserPerfect.setText("去完善");
                    holder.tvItemHbpConsultUserPerfect.setTextColor(RCAndroid.getAttColor(mContext,
                            R.attr.RCDarkColor));
                    holder.tvItemHbpConsultUserPerfect.setEnabled(true);
                } else {
                    holder.tvItemHbpConsultUserPerfect.setText("已完善");
                    holder.tvItemHbpConsultUserPerfect.setTextColor(Color.parseColor("#999999"));
                    holder.tvItemHbpConsultUserPerfect.setEnabled(false);

                    //请求发消息接口
                    mContext.saveSickConsultRecord("-1", dto.getSick_id() + "",
                            "0", "0", "", "", index, holder);
                }

            }
        });

        holder.adapter.setmDatas(list);

        //点击去完善
        holder.tvItemHbpConsultUserPerfect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalIsPrefect)
                    return;
                if (holder.adapter.getSelectUser() < 0) {
                    RCToast.Center(mContext, "请选择您为谁咨询");
                    return;
                }
                mContext.openQuestion(holder.adapter.getSelectUser(),
                        new RCFDHBPConsultProfessorActivity.PrefectQuestionListener() {
                            @Override
                            public void finish(int questionId) {
                                holder.adapter.saveQuestionnaireId(questionId);
                                mDatas.get(index).setSicks(mDatas.get(index).getSicks());
                                holder.tvItemHbpConsultUserPerfect.setText("已完善");
                                holder.tvItemHbpConsultUserPerfect.setTextColor(Color.parseColor("#999999"));
                                holder.tvItemHbpConsultUserPerfect.setEnabled(false);
                                mContext.saveSickConsultRecord("-1", holder.adapter.getSelectUser()
                                        + "", "0", "0", "", "", index, holder);

                            }
                        });
            }
        });

    }

    /**
     * 选择用户后，开始对话，标记为不允许选择
     *
     * @param index 索引index
     */
    public void startTalk(int index, ConsultHolder holder) {
        long selectUser = holder.adapter.getSelectUser();
        ArrayList<RCHBPRecordListDTO.SicksDTO> temp = mDatas.get(index).getSicks();
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).getSick_id() == selectUser) {
                temp.get(i).setChoose(1);
                mDatas.get(index).setSicks(temp);
                notifyItemChanged(index);
                return;
            }
        }

    }


    private void doTypeSystem(SystemHolder holder, RCHBPRecordListDTO s) {
        if (orgId == 1001) {
            holder.ivItemHbpConsultHeadLeft.setImageResource(R.mipmap.rc_fd_ic_head_hbp);
        } else {
            holder.ivItemHbpConsultHeadLeft.setImageResource(R.mipmap.rc_fd_ic_head_hdy);
        }
        if (s.getUpdate_time() > 0) {
            holder.ivItemHbpConsultTimeLeft.setText(RCDateUtil.formatTime(
                    s.getUpdate_time() + "", "HH:mm"));
            holder.ivItemHbpConsultTimeLeft.setVisibility(View.VISIBLE);
        } else {
            holder.ivItemHbpConsultTimeLeft.setVisibility(View.GONE);
        }
        holder.tvItemHbpConsultContentLeft.setText(s.getRecord());
    }

    private void doTypePhoto(PhotoHolder holder, final RCHBPRecordListDTO s) {
        if (orgId == 1001) {
            holder.civItemHbpConsultHeadPhoto.setImageResource(R.mipmap.rc_fd_ic_head_hbp);
        } else {
            holder.civItemHbpConsultHeadPhoto.setImageResource(R.mipmap.rc_fd_ic_head_hdy);
        }

        if (s.getUpdate_time() > 0) {
            holder.tvItemHbpConsultTimeRight.setText(RCDateUtil.formatTime(
                    s.getUpdate_time() + "", "HH:mm"));
            holder.tvItemHbpConsultTimeRight.setVisibility(View.VISIBLE);
        } else {
            holder.tvItemHbpConsultTimeRight.setVisibility(View.GONE);
        }
        RCImageShow.loadUrl(s.getImg_url(), holder.tvItemHbpConsultPhoto, RCImageShow.IMAGE_TYPE_NINE);
        RCImageShow.loadUrl(s.getIcon(), holder.civItemHbpConsultHeadPhoto, RCImageShow.IMAGE_TYPE_HEAD);
        //查看大图
        holder.tvItemHbpConsultPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCPhotoPreviewActivity.goActivity(mContext, s.getImg_url(), true, false);
            }
        });
    }

    private void doTypeUser(UserHolder holder, RCHBPRecordListDTO s) {
        if (orgId == 1001) {
            holder.civItemHbpConsultHeadRight.setImageResource(R.mipmap.rc_fd_ic_head_hbp);
        } else {
            holder.civItemHbpConsultHeadRight.setImageResource(R.mipmap.rc_fd_ic_head_hdy);
        }
        if (s.getUpdate_time() > 0) {
            holder.tvItemHbpConsultTimeRight.setText(RCDateUtil.formatTime(
                    s.getUpdate_time() + "", "HH:mm"));
            holder.tvItemHbpConsultTimeRight.setVisibility(View.VISIBLE);
        } else {
            holder.tvItemHbpConsultTimeRight.setVisibility(View.GONE);
        }
        holder.tvItemHbpConsultContentRight.setText(s.getRecord());
        RCImageShow.loadUrl(s.getIcon(), holder.civItemHbpConsultHeadRight, RCImageShow.IMAGE_TYPE_HEAD);
    }

    private View getView(ViewGroup parent, int layout) {
        return LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
    }


    public void addConsultDataLast(RCHBPRecordListDTO dto) {
        if (dto != null) {
            mDatas.add(dto);
            notifyItemInserted(mDatas.size() - 1);
            mContext.rvHbpConsultProfessor.scrollToPosition(mDatas.size() - 1);
        }
    }


    public void addConsultListData(List<RCHBPRecordListDTO> list) {
        if (list.size() == 0) return;
        if (list.size() == 1) {
            mDatas.add(0, list.get(0));
            notifyItemInserted(0);
        } else {
            mDatas.addAll(0, list);
            notifyItemRangeInserted(0, list.size());
        }
    }


    /**
     * 选择咨询用户样式
     */
    public class ConsultHolder extends RecyclerView.ViewHolder {

        RecyclerView rvItemHbpConsultUser;
        TextView tvItemHbpConsultUserPerfect;
        HBPChooseUserGridAdapter adapter;
        ImageView iv_item_hbp_consult_user_head;

        public ConsultHolder(View itemView) {
            super(itemView);
            rvItemHbpConsultUser = (RecyclerView) itemView.findViewById(R.id.rv_item_hbp_consult_user);
            tvItemHbpConsultUserPerfect = (TextView) itemView.findViewById(R.id.tv_item_hbp_consult_user_perfect);
            iv_item_hbp_consult_user_head = (ImageView) itemView.findViewById(R.id.iv_item_hbp_consult_user_head);
            rvItemHbpConsultUser.setLayoutManager(new GridLayoutManager(mContext, 3));
            rvItemHbpConsultUser.addItemDecoration(new SpaceItemDecoration(3,
                    RCAndroid.dip2px(mContext, 5), RCAndroid.dip2px(mContext, 5)));
            adapter = new HBPChooseUserGridAdapter(mContext);
            rvItemHbpConsultUser.setAdapter(adapter);
        }


    }

    /**
     * 系统（医生）回复的消息的样式
     */
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
            tvItemHbpConsultContentLeft.setBackgroundResource(R.drawable.img_hbp_dialog_right_dy);
        }
    }

    /**
     * 用户发送的消息的样式
     */
    public class UserHolder extends RecyclerView.ViewHolder {
        TextView tvItemHbpConsultTimeRight;
        CircleImageView civItemHbpConsultHeadRight;
        TextView tvItemHbpConsultContentRight;

        public UserHolder(View itemView) {
            super(itemView);
            tvItemHbpConsultTimeRight = (TextView) itemView.findViewById(R.id.tv_item_hbp_consult_time_right);
            civItemHbpConsultHeadRight = (CircleImageView) itemView.findViewById(R.id.civ_item_hbp_consult_head_right);
            tvItemHbpConsultContentRight = (TextView) itemView.findViewById(R.id.tv_item_hbp_consult_content_right);
            tvItemHbpConsultContentRight.setBackgroundResource(R.drawable.img_hbp_dialog_right_dy);
        }
    }

    /**
     * 用户发送的图片的样式
     */
    public class PhotoHolder extends RecyclerView.ViewHolder {
        TextView tvItemHbpConsultTimeRight;
        CircleImageView civItemHbpConsultHeadPhoto;
        ImageView tvItemHbpConsultPhoto;

        PhotoHolder(View view) {
            super(view);
            tvItemHbpConsultTimeRight = view.findViewById(R.id.tv_item_hbp_consult_time_right);
            civItemHbpConsultHeadPhoto = view.findViewById(R.id.civ_item_hbp_consult_head_photo);
            tvItemHbpConsultPhoto = view.findViewById(R.id.tv_item_hbp_consult_photo);
        }
    }


    /**
     * 跳转到编辑资料页面
     *
     * @param context
     * @param listener
     */
    public void gotoEdit(Context context, SaveInfoListener listener) {
        this.listener = listener;
        RCFDAddUserActivity.goActivity(context);
        EventBus.getDefault().register(this);
    }

    private SaveInfoListener listener;

    interface SaveInfoListener {
        void over(boolean isSuccess, String name, long userId);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(JSONObject jsonObject) {
        EventBus.getDefault().unregister(this);
        if (listener != null) {
            if (jsonObject.has("user_id")) {
                listener.over(true, jsonObject.optString("name"),
                        jsonObject.optLong("user_id"));
            } else {
                listener.over(false, "", -1);
            }
        }
    }
}

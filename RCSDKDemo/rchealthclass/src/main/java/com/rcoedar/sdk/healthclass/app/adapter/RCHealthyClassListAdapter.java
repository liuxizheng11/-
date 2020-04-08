package com.rcoedar.sdk.healthclass.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rcoedar.sdk.healthclass.R;
import com.rcoedar.sdk.healthclass.dto.RCHealthClassroomDTO;
import com.rocedar.lib.base.unit.RCImageShow;

import java.util.List;

/**
 * @desc 健康课堂列表
 * @veison V3600
 */

public class RCHealthyClassListAdapter extends BaseAdapter {
    private List<RCHealthClassroomDTO> mDatas;
    private Context context;
    private LayoutInflater inflater;

    public RCHealthyClassListAdapter(List<RCHealthClassroomDTO> mDatas, Context context) {
        this.mDatas = mDatas;
        this.context = context;
        this.inflater = inflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.rc_adapter_health_class, parent, false);
            holder = new ViewHolder();
            //视频
            holder.rc_adapter_health_class = convertView.findViewById(R.id.rc_adapter_health_class);
            holder.rc_adapter_health_video_iv = convertView.findViewById(R.id.rc_adapter_health_video_iv);
            holder.rc_adapter_health_title = convertView.findViewById(R.id.rc_adapter_health_title);
            holder.rc_adapter_health_tag_ll = convertView.findViewById(R.id.rc_adapter_health_tag_ll);
            holder.rc_adapter_health_video_time = convertView.findViewById(R.id.rc_adapter_health_video_time);
            holder.rc_adapter_health_watch_number = convertView.findViewById(R.id.rc_adapter_health_watch_number);

            //图文
            holder.rc_include_adapter_health_class_article = convertView.findViewById(R.id.rc_include_adapter_health_class_article);
            holder.iv_item_consult_img = convertView.findViewById(R.id.rc_iv_item_consult_img);
            holder.iv_item_consult_title = convertView.findViewById(R.id.rc_iv_item_consult_title);
            holder.ll_item_consult_tag = convertView.findViewById(R.id.rc_ll_item_consult_tag);
            holder.iv_item_consult_count = convertView.findViewById(R.id.iv_item_consult_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RCHealthClassroomDTO dto = mDatas.get(position);

        /**
         0,图文；1,视频
         */
        if (dto.getVideo() == 1) {
            holder.rc_adapter_health_class.setVisibility(View.VISIBLE);
            holder.rc_include_adapter_health_class_article.setVisibility(View.GONE);

            RCImageShow.loadUrl(dto.getThumbnail(), holder.rc_adapter_health_video_iv, RCImageShow.IMAGE_TYPE_ALBUM, R.mipmap.rc_shop_placeholder);
            holder.rc_adapter_health_title.setText(dto.getTitle());
            holder.rc_adapter_health_watch_number.setText(dto.getRead_num());
            //动态添加标签
            holder.rc_adapter_health_tag_ll.removeAllViews();
            //视频时间
            holder.rc_adapter_health_video_time.setText(secToTime(dto.getVideo_time()));
//            for (int i = 0; i < dto.getSubject_name_list().size(); i++) {

            View view = inflater.inflate(R.layout.rc_include_health_tag_list, null);
            TextView tv_home_tag = view.findViewById(R.id.rc_adapter_health_tag);
            tv_home_tag.setText(dto.getSubject_name_list().get(0));
            holder.rc_adapter_health_tag_ll.addView(view);
//            }
        } else {
            holder.rc_adapter_health_class.setVisibility(View.GONE);
            holder.rc_include_adapter_health_class_article.setVisibility(View.VISIBLE);

            RCImageShow.loadUrl(dto.getThumbnail(), holder.iv_item_consult_img, RCImageShow.IMAGE_TYPE_STAR);
            holder.iv_item_consult_title.setText(dto.getTitle());
            holder.iv_item_consult_count.setText(dto.getRead_num());
            //动态添加标签
            holder.ll_item_consult_tag.removeAllViews();

            for (int i = 0; i < dto.getSubject_name_list().size(); i++) {

                View view = inflater.inflate(R.layout.rc_include_health_class_article_text, null);
                TextView tv_home_tag = (TextView) view.findViewById(R.id.rc_health_class_article_text);
                tv_home_tag.setText(dto.getSubject_name_list().get(i));
                holder.ll_item_consult_tag.addView(view);
            }
        }


        return convertView;
    }

    static class ViewHolder {
        //视频布局
        RelativeLayout rc_adapter_health_class;
        ImageView rc_adapter_health_video_iv;
        TextView rc_adapter_health_title;
        TextView rc_adapter_health_video_time;
        LinearLayout rc_adapter_health_tag_ll;
        TextView rc_adapter_health_watch_number;
        //图文布局
        RelativeLayout rc_include_adapter_health_class_article;
        ImageView iv_item_consult_img;
        TextView iv_item_consult_title;
        LinearLayout ll_item_consult_tag;
        TextView iv_item_consult_count;
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
}

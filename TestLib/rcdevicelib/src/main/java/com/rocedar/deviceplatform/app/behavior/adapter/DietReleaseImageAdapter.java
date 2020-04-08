package com.rocedar.deviceplatform.app.behavior.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.rocedar.base.RCImageShow;
import com.rocedar.deviceplatform.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：FangZhou2.1
 * <p>
 * 作者：phj
 * 日期：2017/8/2 下午5:20
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class DietReleaseImageAdapter extends BaseAdapter {

    public static final String APPLICATION_NAME = "myApp";
    //单次最多发送图片数
    public static final int MAX_IMAGE_SIZE = 9;
    //首选项:临时图片
    public static final String PREF_TEMP_IMAGES = "pref_temp_images";

    private List<String> mDataList = new ArrayList<>();
    private Context mContext;

    public DietReleaseImageAdapter(Context context, List<String> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }

    public int getCount() {
        // 多返回一个用于展示添加图标
        if (mDataList == null) {
            return 1;
        } else if (mDataList.size() == MAX_IMAGE_SIZE) {
            return MAX_IMAGE_SIZE;
        } else {
            return mDataList.size() + 1;
        }
    }

    public Object getItem(int position) {
        if (mDataList != null
                && mDataList.size() == MAX_IMAGE_SIZE) {
            return mDataList.get(position);
        } else if (mDataList == null || position - 1 < 0
                || position > mDataList.size()) {
            return null;
        } else {
            return mDataList.get(position - 1);
        }
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    public View getView(final int position, View convertView, ViewGroup parent) {
        // 所有Item展示不满一页，就不进行ViewHolder重用了，避免了一个拍照以后添加图片按钮被覆盖的奇怪问题
        convertView = View.inflate(mContext, R.layout.adapter_record_image, null);
        ImageView imageIv = (ImageView) convertView.findViewById(R.id.release_image);
        ImageView button = (ImageView) convertView.findViewById(R.id.release_image_close);
        if (isShowAddItem(position)) {
            imageIv.setImageResource(R.mipmap.ic_release_add_image);
            button.setVisibility(View.GONE);
        } else {
            final String item = mDataList.get(position);
            RCImageShow.loadFilethumbnail(item, imageIv);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDataList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

        return convertView;
    }

    private boolean isShowAddItem(int position) {
        int size = mDataList == null ? 0 : mDataList.size();
        return position == size;
    }
}

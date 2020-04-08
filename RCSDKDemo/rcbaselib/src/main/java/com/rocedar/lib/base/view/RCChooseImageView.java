package com.rocedar.lib.base.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rocedar.lib.base.R;
import com.rocedar.lib.base.unit.RCPhotoChooseUtil;
import com.rocedar.lib.base.unit.RCPhotoPreViewUtil;
import com.rocedar.lib.base.image.upyun.UploadImage;
import com.rocedar.lib.base.unit.RCImageShow;
import com.rocedar.lib.base.unit.RCLog;
import com.rocedar.lib.sdk.rcgallery.dto.RCPhotoDTO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/7/17 下午3:41
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCChooseImageView extends LinearLayout {

    //单次最多发送图片数
    private int MAX_IMAGE_SIZE = 9;

    public RCChooseImageView(Context context) {
        super(context);
        init(context, null);
    }

    public RCChooseImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RCChooseImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RCChooseImageView);
            MAX_IMAGE_SIZE = a.getInteger(R.styleable.RCChooseImageView_rc_max_choose_number, MAX_IMAGE_SIZE);
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.rc_gallery_choose_view, this);
        GridView gridView = view.findViewById(R.id.rc_gallery_choose_view_grid);
        initGridViewView(gridView);
    }

    /**
     * 设置最大可选数量
     *
     * @param maxNumber
     */
    public void setMaxImageSize(int maxNumber) {
        this.MAX_IMAGE_SIZE = maxNumber;
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 设置已经选择的图片对象
     *
     * @param list
     */
    public void setUrlPathList(List<RCPhotoDTO> list) {
        urlPathList.clear();
        urlPathList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 设置已经选择的图片
     *
     * @param list      图片地址
     * @param isNetWork 是否是网络图片
     */
    public void setUrlPathList(List<String> list, boolean isNetWork) {
        urlPathList.clear();
        for (int i = 0; i < list.size(); i++) {
            urlPathList.add(new RCPhotoDTO(list.get(i), isNetWork));
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 设置已经选择的图片
     *
     * @param list      图片地址
     * @param isNetWork 是否是网络图片
     */
    public void setUrlPathList(String[] list, boolean isNetWork) {
        urlPathList.clear();
        for (int i = 0; i < list.length; i++) {
            urlPathList.add(new RCPhotoDTO(list[i], isNetWork));
        }
        mAdapter.notifyDataSetChanged();
    }


    private ChooseChangeListener chooseChangeListener;

    public void setChooseChangeListener(ChooseChangeListener chooseChangeListener) {
        this.chooseChangeListener = chooseChangeListener;
    }

    /**
     * 获取选择的图片对象
     *
     * @return
     */
    public List<RCPhotoDTO> getUrlPathList() {
        return urlPathList;
    }

    /**
     *
     * 调用选择照片
     *
     * @return true为正常打开，false为选择照片的数量达到上限
     */
    public boolean clickChoose() {
        if (MAX_IMAGE_SIZE > urlPathList.size()) {
            new RCPhotoChooseUtil(getContext()).goChoose(MAX_IMAGE_SIZE - urlPathList.size(),
                    new RCPhotoChooseUtil.ChooseAlbumListener() {
                        @Override
                        public void over(List<String> chooseList) {
                            for (int i = 0; i < chooseList.size(); i++) {
                                urlPathList.add(new RCPhotoDTO(chooseList.get(i), false));
                            }
                            mAdapter.notifyDataSetChanged();
                            if (chooseChangeListener != null)
                                chooseChangeListener.chooseChange(urlPathList);
                        }
                    });
            return true;
        }
        return false;
    }


    //是否是正在上传图片
    private boolean upload = false;


    /**
     * 开始上传图片
     *
     * @param upLoadListener
     */
    public void startUpload(UpLoadListener upLoadListener) {
        upload = true;
        upLoadImage(upLoadListener);
    }

    /**
     * 停止上传图片
     */
    public void stopUpload() {
        upload = false;
    }


    public interface UpLoadListener {

        void upLoadStart();

        void upLoadOver(ArrayList<RCPhotoDTO> urlPathList);

        void upLoadError();

        void upLoadClose();

    }


    public interface ChooseChangeListener {

        void chooseChange(ArrayList<RCPhotoDTO> urlPathList);
    }


    //上传图片方法
    private void upLoadImage(UpLoadListener upLoadListener) {
        if (!upload) {
            upLoadListener.upLoadClose();
            return;
        }
        upLoadListener.upLoadStart();
        for (int i = 0; i < urlPathList.size(); i++) {
            if (!urlPathList.get(i).isNetwork()) {
                doUpload(upLoadListener, i);
                return;
            }
        }
        upLoadListener.upLoadOver(urlPathList);
    }

    private void doUpload(final UpLoadListener upLoadListener, final int index) {
        new UploadImage(new UploadImage.UploadListener() {

            @Override
            public void onProgressListener(int p) {
                RCLog.i("图片上传中（" + index + "）" + p);
            }

            @Override
            public void onUpLoadOverOk(String url, int imageW, int imageH) {
                urlPathList.get(index).setPath(url);
                urlPathList.get(index).setNetwork(true);
                upLoadImage(upLoadListener);
            }

            @Override
            public void onUpLoadOverError() {
                upLoadListener.upLoadError();
            }
        }, new File(urlPathList.get(index).getPath()), UploadImage.Models.UserCircle);
    }


    private ChooseImageAdapter mAdapter;

    //选择的图片地址
    private ArrayList<RCPhotoDTO> urlPathList = new ArrayList<>();

    private void initGridViewView(GridView gridView) {
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new ChooseImageAdapter(getContext(), urlPathList);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == urlPathList.size()) {//点击弹出选项框,打开相机
                    new RCPhotoChooseUtil(getContext()).goChoose(MAX_IMAGE_SIZE - urlPathList.size(),
                            new RCPhotoChooseUtil.ChooseAlbumListener() {
                                @Override
                                public void over(List<String> chooseList) {
                                    for (int i = 0; i < chooseList.size(); i++) {
                                        urlPathList.add(new RCPhotoDTO(chooseList.get(i), false));
                                    }
                                    mAdapter.notifyDataSetChanged();
                                    if (chooseChangeListener != null)
                                        chooseChangeListener.chooseChange(urlPathList);
                                }
                            });
                } else {//点进查看照片的viewpager
                    new RCPhotoPreViewUtil(getContext()).goPreview(urlPathList, position,
                            new RCPhotoPreViewUtil.PreviewListener() {
                                @Override
                                public void over(List<RCPhotoDTO> chooseList) {
                                    urlPathList.clear();
                                    for (int i = 0; i < chooseList.size(); i++) {
                                        urlPathList.add(chooseList.get(i));
                                    }
                                    mAdapter.notifyDataSetChanged();
                                    if (chooseChangeListener != null)
                                        chooseChangeListener.chooseChange(urlPathList);
                                }
                            });
                }
            }
        });
    }


    private class ChooseImageAdapter extends BaseAdapter {


        private List<RCPhotoDTO> mDataList = new ArrayList<>();
        private Context mContext;

        public ChooseImageAdapter(Context context, List<RCPhotoDTO> dataList) {
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
            convertView = View.inflate(mContext, R.layout.rc_gallery_adapter_choose_view, null);
            ImageView imageIv = (ImageView) convertView.findViewById(R.id.rc_gallery_adapter_choose_view_add);
            ImageView button = (ImageView) convertView.findViewById(R.id.rc_gallery_adapter_choose_view_delete);
            if (isShowAddItem(position)) {
                imageIv.setImageResource(R.mipmap.rc_gallery_choose_add);
                button.setVisibility(View.GONE);
            } else {
                String item = mDataList.get(position).getPath();
                if (mDataList.get(position).isNetwork()) {
                    RCImageShow.loadUrl(item, imageIv, RCImageShow.IMAGE_TYPE_NINE);
                } else {
                    RCImageShow.loadFilethumbnail(item, imageIv);
                }
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

}

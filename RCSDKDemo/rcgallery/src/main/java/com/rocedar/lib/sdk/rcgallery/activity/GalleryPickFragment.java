package com.rocedar.lib.sdk.rcgallery.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rocedar.lib.sdk.rcgallery.R;
import com.rocedar.lib.sdk.rcgallery.adapter.FolderAdapter;
import com.rocedar.lib.sdk.rcgallery.adapter.PhotoAdapter;
import com.rocedar.lib.sdk.rcgallery.dto.FolderInfo;
import com.rocedar.lib.sdk.rcgallery.dto.PhotoInfo;
import com.rocedar.lib.sdk.rcgallery.config.GalleryConfig;
import com.rocedar.lib.sdk.rcgallery.config.GalleryPick;
import com.rocedar.lib.sdk.rcgallery.config.PhotoConfig;
import com.rocedar.lib.sdk.rcgallery.inter.IHandlerCallBack;
import com.rocedar.lib.sdk.rcgallery.utils.FileUtils;
import com.rocedar.lib.sdk.rcgallery.widget.FolderListPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/4 下午6:49
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class GalleryPickFragment extends Fragment {


    public static GalleryPickFragment newInstance(boolean isOpenCamera) {
        Bundle args = new Bundle();
        args.putBoolean("isOpenCamera", isOpenCamera);
        GalleryPickFragment fragment = new GalleryPickFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private Context mContext = null;
    private Activity mActivity = null;
    private final static String TAG = "GalleryPickActivity";

    private ArrayList<String> resultPhoto;

    private RecyclerView rvGalleryImage;        // 图片列表

    private PhotoAdapter photoAdapter;              // 图片适配器
    private FolderAdapter folderAdapter;            // 文件夹适配器


    private List<FolderInfo> folderInfoList = new ArrayList<>();    // 本地文件夹信息List
    private List<PhotoInfo> photoInfoList = new ArrayList<>();      // 本地图片信息List

    private static final int LOADER_ALL = 0;         // 获取所有图片
    private static final int LOADER_CATEGORY = 1;    // 获取某个文件夹中的所有图片

    private boolean hasFolderScan = false;           // 是否扫描过

    private GalleryConfig galleryConfig;   // GalleryPick 配置器

    private static final int REQUEST_CAMERA = 100;   // 设置拍摄照片的 REQUEST_CODE

    private IHandlerCallBack mHandlerCallBack;   // GalleryPick 生命周期接口

    private FolderListPopupWindow folderListPopupWindow;   // 文件夹选择弹出框

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_main, null);
        initView(view);
        mContext = getActivity();
        mActivity = getActivity();
        galleryConfig = GalleryPick.getInstance().getGalleryConfig();
        if (galleryConfig == null) {
            mHandlerCallBack.onGalleryError();
        }
        boolean isOpenCamera = getArguments().getBoolean("isOpenCamera", false);
        if (isOpenCamera || galleryConfig.isOpenCamera()) {
            galleryConfig.getBuilder().isOpenCamera(true).build();
            showCameraAction();
        }
        init();
        initPhoto();
        return view;
    }


    private boolean sIsScrolling = false;

    /**
     * 初始化视图
     */
    private void initView(View view) {
        rvGalleryImage = (RecyclerView) view.findViewById(R.id.rvGalleryImage);
        rvGalleryImage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (sIsScrolling == true) {
                        GalleryPick.getInstance().getGalleryConfig().getImageLoader().resumeRequests(mContext);
                    }
                    sIsScrolling = false;
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    sIsScrolling = true;
                    GalleryPick.getInstance().getGalleryConfig().getImageLoader().pauseRequests(mContext);
                }
            }
        });
    }


    /**
     * 初始化
     */
    private void init() {
        mHandlerCallBack = galleryConfig.getIHandlerCallBack();
        resultPhoto = galleryConfig.getPathList();
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        rvGalleryImage.setLayoutManager(gridLayoutManager);
        photoAdapter = new PhotoAdapter(mActivity, mContext, photoInfoList);
        photoAdapter.setOnCallBack(new PhotoAdapter.OnCallBack() {
            @Override
            public void OnClickCamera(List<String> selectPhotoList) {
                resultPhoto.clear();
                resultPhoto.addAll(selectPhotoList);
                showCameraAction();
            }

            @Override
            public void OnClickPhoto(List<String> selectPhotoList) {
                resultPhoto.clear();
                resultPhoto.addAll(selectPhotoList);

                if (!galleryConfig.isMultiSelect() && resultPhoto != null && resultPhoto.size() > 0) {
                    if (galleryConfig.isCrop()) {
                        cameraTempFile = new File(resultPhoto.get(0));
                        cropTempFile = FileUtils.getCorpFile(galleryConfig.getFilePath());
                        cropPhoto(cameraTempFile, cropTempFile);
                        return;
                    }
                    mHandlerCallBack.onGallerySuccess(resultPhoto);
                } else {
                    mHandlerCallBack.chooseChange(selectPhotoList, galleryConfig.getMaxSize());
                }

            }

        });
        photoAdapter.setSelectPhoto(resultPhoto);
        rvGalleryImage.setAdapter(photoAdapter);

        folderAdapter = new FolderAdapter(mActivity, mContext, folderInfoList);
        folderAdapter.setOnClickListener(new FolderAdapter.OnClickListener() {
            @Override
            public void onClick(FolderInfo folderInfo) {
                if (folderInfo == null) {
                    getActivity().getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                    mHandlerCallBack.chooseFolder(getString(R.string.gallery_all_folder), false);
                } else {
                    photoInfoList.clear();
                    photoInfoList.addAll(folderInfo.photoInfoList);
                    photoAdapter.notifyDataSetChanged();
                    mHandlerCallBack.chooseFolder(folderInfo.name, false);
                }
                folderListPopupWindow.dismiss();
                gridLayoutManager.scrollToPosition(0);
            }
        });
    }

    public void setSelectPhoto(List<String> photoList) {
        resultPhoto.clear();
        resultPhoto.addAll(photoList);
        photoAdapter.notifyDataSetChanged();
        mHandlerCallBack.chooseChange(resultPhoto, galleryConfig.getMaxSize());
    }


    public void openFolder(View tvGalleryFolder) {
        if (folderListPopupWindow != null && folderListPopupWindow.isShowing()) {
            folderListPopupWindow.dismiss();
            mHandlerCallBack.chooseFolder(null, false);
            return;
        }
        folderListPopupWindow = new FolderListPopupWindow(mActivity, mContext, folderAdapter);
        folderListPopupWindow.showAsDropDown(tvGalleryFolder);
        mHandlerCallBack.chooseFolder(null, true);
    }


    /**
     * 初始化配置
     */
    private void initPhoto() {
        mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
            private final String[] IMAGE_PROJECTION = {
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.SIZE
            };

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                if (id == LOADER_ALL) {
                    return new CursorLoader(mActivity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[2] + " DESC");
                } else if (id == LOADER_CATEGORY) {
                    return new CursorLoader(mActivity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
                }

                return null;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (data != null) {
                    int count = data.getCount();
                    if (count > 0) {
                        List<PhotoInfo> tempPhotoList = new ArrayList<>();
                        data.moveToFirst();
                        do {
                            String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                            String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                            long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                            int size = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                            boolean showFlag = size > 1024 * 5;                           //是否大于5K
                            PhotoInfo photoInfo = new PhotoInfo(path, name, dateTime);
                            if (showFlag) {
                                tempPhotoList.add(photoInfo);
                            }
                            if (!hasFolderScan && showFlag) {
                                File photoFile = new File(path);                  // 获取图片文件
                                File folderFile = photoFile.getParentFile();      // 获取图片上一级文件夹

                                FolderInfo folderInfo = new FolderInfo();
                                folderInfo.name = folderFile.getName();
                                folderInfo.path = folderFile.getAbsolutePath();
                                folderInfo.photoInfo = photoInfo;
                                if (!folderInfoList.contains(folderInfo)) {      // 判断是否是已经扫描到的图片文件夹
                                    List<PhotoInfo> photoInfoList = new ArrayList<>();
                                    photoInfoList.add(photoInfo);
                                    folderInfo.photoInfoList = photoInfoList;
                                    folderInfoList.add(folderInfo);
                                } else {
                                    FolderInfo f = folderInfoList.get(folderInfoList.indexOf(folderInfo));
                                    f.photoInfoList.add(photoInfo);
                                }
                            }

                        } while (data.moveToNext());

                        photoInfoList.clear();
                        photoInfoList.addAll(tempPhotoList);

                        List<String> tempPhotoPathList = new ArrayList<>();
                        for (PhotoInfo photoInfo : photoInfoList) {
                            tempPhotoPathList.add(photoInfo.path);
                        }
                        for (String mPhotoPath : galleryConfig.getPathList()) {
                            if (!tempPhotoPathList.contains(mPhotoPath)) {
                                PhotoInfo photoInfo = new PhotoInfo(mPhotoPath, null, 0L);
                                photoInfoList.add(0, photoInfo);
                            }
                        }


                        photoAdapter.notifyDataSetChanged();
                        hasFolderScan = true;
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);   // 扫描手机中的图片
    }


    private File cameraTempFile;
    private File cropTempFile;

    /**
     * 选择相机
     */
    private void showCameraAction() {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            // 创建临时文件

            cameraTempFile = FileUtils.createTmpFile(mActivity, galleryConfig.getFilePath());
            Uri imageUri = FileProvider.getUriForFile(mActivity, galleryConfig.getProvider(), cameraTempFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                mContext.grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(mContext, R.string.gallery_msg_no_camera, Toast.LENGTH_SHORT).show();
            galleryConfig.getIHandlerCallBack().onGalleryError();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == mActivity.RESULT_OK) {
                if (cameraTempFile != null) {
                    if (!galleryConfig.isMultiSelect()) {
                        resultPhoto.clear();
                        if (galleryConfig.isCrop()) {
                            cropTempFile = FileUtils.getCorpFile(galleryConfig.getFilePath());
                            cropPhoto(cameraTempFile, cropTempFile);
                            return;
                        }
                    }
                    resultPhoto.add(cameraTempFile.getAbsolutePath());

                    // 通知系统扫描该文件夹
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(new File(FileUtils.getFilePath(mContext) + galleryConfig.getFilePath()));
                    intent.setData(uri);
                    mContext.sendBroadcast(intent);
                    mHandlerCallBack.onGallerySuccess(resultPhoto);
                }
            } else {
                if (cameraTempFile != null && cameraTempFile.exists()) {
                    cameraTempFile.delete();
                }
//                if (galleryConfig.isOpenCamera()) {
//                    exit();
//                }
            }
        } else if (resultCode == mActivity.RESULT_OK && requestCode == PhotoConfig.REQUESTCODE_PHOTO_PICTURE_CUT) {
            resultPhoto.clear();
            resultPhoto.add(cropTempFile.getAbsolutePath());
            mHandlerCallBack.onGallerySuccess(resultPhoto);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 回退键监听
     */
    public boolean onBack() {
        if (folderListPopupWindow != null && folderListPopupWindow.isShowing()) {
            folderListPopupWindow.dismiss();
            return true;
        }
        return false;
    }

    /**
     * 裁剪
     */
    private void cropPhoto(File sourceFile, File destinationFile) {
        Uri outputUri = Uri.fromFile(destinationFile);//缩略图保存地址
        Uri inputUri;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            inputUri = Uri.fromFile(sourceFile);
        } else {
            inputUri = FileProvider.getUriForFile(mContext, galleryConfig.getProvider(), sourceFile);
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(inputUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, PhotoConfig.REQUESTCODE_PHOTO_PICTURE_CUT);
    }

}

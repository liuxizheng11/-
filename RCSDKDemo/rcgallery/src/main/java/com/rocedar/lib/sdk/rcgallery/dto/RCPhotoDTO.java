package com.rocedar.lib.sdk.rcgallery.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/7/16 下午3:02
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCPhotoDTO implements Parcelable{

    public String path;

    public boolean network;

    public RCPhotoDTO() {
    }

    public RCPhotoDTO(String path, boolean network) {
        this.path = path;
        this.network = network;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isNetwork() {
        return network;
    }

    public void setNetwork(boolean network) {
        this.network = network;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeByte((byte) (network ? 1 : 0));
    }

    protected RCPhotoDTO(Parcel in) {
        path = in.readString();
        network = in.readByte() != 0;
    }

    public static final Creator<RCPhotoDTO> CREATOR = new Creator<RCPhotoDTO>() {
        @Override
        public RCPhotoDTO createFromParcel(Parcel in) {
            return new RCPhotoDTO(in);
        }

        @Override
        public RCPhotoDTO[] newArray(int size) {
            return new RCPhotoDTO[size];
        }
    };
}

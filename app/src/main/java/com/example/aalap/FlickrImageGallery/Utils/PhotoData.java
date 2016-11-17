package com.example.aalap.FlickrImageGallery.Utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Aalap on 2016-11-13.
 */

public class PhotoData implements Parcelable {

    private String ownerName;
    private String caption;
    private String thumbnail;
    private String largeImage;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLargeImage() {
        return largeImage;
    }

    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setSmallImage(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void removeItem(String largeImage){

    }

    public PhotoData(){}

    public PhotoData(Parcel in) {
        caption=in.readString();
        thumbnail=in.readString();
        largeImage=in.readString();
        ownerName=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeString(thumbnail);
        dest.writeString(largeImage);
        dest.writeString(ownerName);
    }

    public static final Creator<PhotoData> CREATOR = new Creator<PhotoData>() {
        @Override
        public PhotoData createFromParcel(Parcel in) {
            return new PhotoData(in);
        }

        @Override
        public PhotoData[] newArray(int size) {
            return new PhotoData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


}

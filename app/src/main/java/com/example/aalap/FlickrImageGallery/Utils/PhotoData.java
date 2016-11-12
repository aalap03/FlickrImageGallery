package com.example.aalap.FlickrImageGallery.Utils;

import java.io.Serializable;

/**
 * Created by Aalap on 2016-09-08.
 */
public class PhotoData implements Serializable {

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

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}

package ripe.ripe;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;

public class UserDataModel {

    String title;
    int totalLikes;
    Bitmap photo;
    AssetFileDescriptor video;
    Boolean isVideo;

    public String getTitle() {
        return title;
    }
    public void setName(String name) {
        this.title = name;
    }

    public int getTotalLikes() {
        return totalLikes;
    }
    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public Bitmap getPhoto() {
        return photo;
    }
    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public AssetFileDescriptor getVideo() { return video; }
    public void setVideo(AssetFileDescriptor vid) { this.video = vid; }

    public Boolean getIsVideo() { return isVideo; }
    public void setIsVideo(Boolean isVid) { this.isVideo = isVid; }
}

package ripe.ripe;

import android.graphics.Bitmap;

public class UserDataModel {

    String title;
    int totalLikes;
    Bitmap photo;
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

}

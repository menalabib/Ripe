package ripe.ripe.APIUtils;

import java.io.File;
import java.util.ArrayList;

public class RipeContent {

    public String is_video;
    public String title;
    public String uid;
    public ArrayList<String> tags;
    public String uploaded_by;
    public Integer upvotes;
    public Integer downvotes;
    public Integer views;
    public File file;

    public RipeContent (String is_video, String title, ArrayList<String> tags, String uploaded_by, File file) {
        this.is_video = is_video;
        this.title = title;
        this.tags = tags;
        this.uploaded_by = uploaded_by;
        this.file = file;
    }

    public String splitTags() {
        String split = "";

        for(String s: this.tags) {
            split += s+",";
        }
        return split.substring(0, split.lastIndexOf(","));
    }

}
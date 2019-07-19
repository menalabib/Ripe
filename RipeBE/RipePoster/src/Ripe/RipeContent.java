package Ripe;

import java.io.File;
import java.util.ArrayList;

public class RipeContent {

    public String title;
    public String uuid;
    public ArrayList<String> tags;
    public String uploaded_by;
    public Integer upvotes;
    public Integer downvotes;
    public Integer views;
    public File file;

    RipeContent (String title, String uuid, ArrayList<String> tags, String uploaded_by, Integer upvotes, Integer downvotes,
                 Integer views, File file) {
        this.title = title;
        this.uuid = uuid;
        this.tags = tags;
        this.uploaded_by = uploaded_by;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.views = views;
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
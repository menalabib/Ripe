package Ripe;

import java.io.File;

public class RipeContent {

    public String title;
    public String uuid;
    public String tags;
    public String uploaded_by;
    public Integer upvotes;
    public Integer downvotes;
    public Integer views;
    public File file;

    RipeContent (String title, String uuid, String tags, String uploaded_by, Integer upvotes, Integer downvotes, Integer views, File file) {
        this.title = title;
        this.uuid = uuid;
        this.tags = tags;
        this.uploaded_by = uploaded_by;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.views = views;
        this.file = file;
    }

}
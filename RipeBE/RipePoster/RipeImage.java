package Ripe;

private String title;
private String uuid;
private String[] tags;
private String uploaded_by;
private Integer upvotes;
private Integer downvotes;
private Integer views;
private File file;

public class RipeImage {
    RipeImage (String title, String uuid, String[] tags, String uploaded_by, Integer upvotes, Integer downvotes, Integer views, File file) {
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
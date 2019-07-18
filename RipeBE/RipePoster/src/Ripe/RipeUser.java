package Ripe;

public class RipeUser {
    public String name;
    public String email;
    public String[] contentUploaded;
    public Integer score;
    public String[] savedContent;
    public String[] viewedContent;

    RipeUser (String name, String email, String[] contentUploaded, Integer score, String[] savedContent, String[] viewedContent) {
        this.name = name;
        this.email = email;
        this.contentUploaded = contentUploaded;
        this.score = score;
        this.savedContent = savedContent;
        this.viewedContent = viewedContent;
    }
}

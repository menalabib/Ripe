package ripe.ripe.APIUtils;

public class RipeUser {
    public String uuid;
    public String name;
    public String email;
    public String[] content_uploaded;
    public Integer score;
    public String[] saved_content;
    public String[] viewed_content;
    public String[] tags;

    public RipeUser (String name, String email, String[] contentUploaded, Integer score, String[] savedContent, String[] viewedContent,
              String uuid) {
        this.name = name;
        this.email = email;
        this.content_uploaded = contentUploaded;
        this.score = score;
        this.saved_content = savedContent;
        this.viewed_content = viewedContent;
        this.uuid = uuid;
    }

    RipeUser() {

    }

    public void setUuid(String id) {
        uuid = id;
    }

    public void copy(RipeUser ru) {
        this.name = ru.name;
        this.email = ru.email;
        this.content_uploaded = ru.content_uploaded;
        this.score = ru.score;
        this.saved_content = ru.saved_content;
        this.viewed_content = ru.viewed_content;
        this.uuid = ru.uuid;
        this.tags = ru.tags;
    }

}

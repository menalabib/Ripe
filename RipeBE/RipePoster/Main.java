package Ripe;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Body;
import retrofit2.Call;
import retrofit2.Converter;
import java.net.URL;
import java.io.File;
import java.io.InputStream;

public class Main {


    private class ContentPost {
        private String title;
        private String uuid;
        private String[] tags;
        private String uploaded_by;
        private Integer upvotes;
        private Integer downvotes;
        private Integer views;

        ContentPost(String title, String uuid, String[] tags,
                    String uploaded_by, Integer upvotes, Integer downvotes,
                    Integer views) {
            this.title = title;
            this.uuid = uuid;
            this.tags = tags;
            this.uploaded_by = uploaded_by;
            this.upvotes = upvotes;
            this.downvotes = downvotes;
            this.views = views;
        }
    }

    private void uploadPic() throws Exception {
        URL url = new URL("http://127.0.0.1:5000/");
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(url)
                .build();

        RipeService service = retrofit.create(RipeService.class);

        // Request Data
        RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"), "Title");

        RequestBody uid = RequestBody.create(MediaType.parse("multipart/form-data"), "1");

        RequestBody tags = RequestBody.create(MediaType.parse("multipart/form-data"), "tag");

        RequestBody ub = RequestBody.create(MediaType.parse("multipart/form-data"), "Shak");

        RequestBody uv = RequestBody.create(MediaType.parse("multipart/form-data"), "2");

        RequestBody dv = RequestBody.create(MediaType.parse("multipart/form-data"), "1");

        RequestBody views = RequestBody.create(MediaType.parse("multipart/form-data"), "123");

        // Getting file info
        File file = new File(getClass().getClassLoader().getResource("test.jpg").getFile());
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        service.postContent(title, uid, tags, ub, uv, dv, views, body).execute();
        System.out.println("Donzo");
    }

    public static void main(String args[]) throws Exception {
        Main main = new Main();
        main.uploadPic();
    }
}

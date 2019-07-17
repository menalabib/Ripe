package Ripe;

import Ripe.RipeContent;
import java.io.File;
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
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import java.net.URL;

public class RipePoster {
    private final URL ripeURL;
    private final Retrofit retrofit;
    private final RipeService ripeService;

    RipePoster(URL url) {
        this.ripeURL = url;

        this.retrofit = new Retrofit
                .Builder()
                .baseUrl(url)
                .build();

        this.ripeService = retrofit.create(RipeService.class);
    }

    private interface RipeService {
        @Multipart
        @POST("post_content")
        Call<ResponseBody> postContent(
                @Part("title") RequestBody title,
                @Part("uid") RequestBody uid,
                @Part("tags") RequestBody tags,
                @Part("uploaded_by") RequestBody ub,
                @Part("upvotes") RequestBody uv,
                @Part("downvotes") RequestBody dv,
                @Part("views") RequestBody v,
                @Part MultipartBody.Part image
        );
    }

    public void uploadContent(RipeContent ripeContent) throws Exception {
        // Request Data
        RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.title);

        RequestBody uid = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.uuid.toString());

        RequestBody tags = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.tags);

        RequestBody ub = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.uploaded_by);

        RequestBody uv = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.upvotes.toString());

        RequestBody dv = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.downvotes.toString());

        RequestBody views = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.views.toString());

        // Getting file info
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", ripeContent.file.getName(), requestFile);

        //ripeService.postContent(title, uid, tags, ub, uv, dv, views, body);
        Call<ResponseBody> response = ripeService.postContent(title, uid, tags, ub, uv, dv, views, body);
        response.enqueue(new Callback<ResponseBody> () {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.print(response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.print("failed to post content");
            }
        });

    }

}
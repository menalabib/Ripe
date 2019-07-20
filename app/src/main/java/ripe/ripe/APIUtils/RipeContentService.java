package ripe.ripe.APIUtils;

import android.util.Log;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.http.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RipeContentService {
    private Retrofit retrofit;
    private RipeService ripeService;
    private final String url = "http://172.20.10.4:5000/";

    public RipeContentService() {
        try {
            this.retrofit = new Retrofit
                    .Builder()
                    .baseUrl(new URL(url))
                    .build();
            this.ripeService = retrofit.create(RipeService.class);
        }
        catch (MalformedURLException e) { }
    }

    public interface RipeCallback {
        void setContent(String contentId, String title, boolean isVideo);
    }

    public interface GetContentByIdCallback{
        void populateContent(RipeContent content);
    }

    private interface RipeService {
        @Multipart
        @POST("post_content")
        Call<ResponseBody> postContent(
                @Part("is_video") RequestBody isVideo,
                @Part("title") RequestBody title,
                @Part("tags") RequestBody tags,
                @Part("uploaded_by") RequestBody ub,
                @Part MultipartBody.Part file
        );

        @GET("get_content_for_user/{uuid}")
        Call<ResponseBody> getContentForUser(
                @Path("uuid") String uuid
        );

        @GET("get_content_by_id/{contentId}")
        Call<ResponseBody> getContentById(
                @Path("contentId") String contentId
        );

        @Multipart
        @PUT("update_content_view/{userId}/{contentId}")
        Call<ResponseBody> updateContentViews(
                @Path("userId") String userId,
                @Path("contentId") String contentId,
                @Part("action") RequestBody action
        );
    }

    public void getContentById(String contentId, final GetContentByIdCallback getContentByIdCallback) {
        final Type ripeContentType = new TypeToken<RipeContent>(){}.getType();
        Call<ResponseBody> responseBodyCall = ripeService.getContentById(contentId);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    RipeContent content = new Gson().fromJson(response.body().string(), ripeContentType);
                    getContentByIdCallback.populateContent(content);
                } catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void updateContentViews(String userId, String contentId, Integer vote) {
        RequestBody action = RequestBody.create(MediaType.parse("multipart/form-data"), vote.toString());
        Call<ResponseBody> response = ripeService.updateContentViews(userId, contentId, action);
        response.enqueue(
            new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    System.out.println("updated content view");
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.print("failed to update content view");
                }
            }
        );
    }

    public void getContentForUser(String uuid, final RipeCallback ripeCallback) {
        final Type listType = new TypeToken<List<String>>(){}.getType();
        Call<ResponseBody> response = ripeService.getContentForUser(uuid);
        response.enqueue(new Callback<ResponseBody> () {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.print("GetContentForUser response: " + response.toString());
                if(response.code() == 400) return;
                try {
                    List<String> l = new Gson().fromJson(response.body().string(), listType);
                    ripeCallback.setContent(l.get(0), l.get(1), l.get(2).equals("1"));
                } catch (Exception e) {
                    System.out.println("No content return");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.print("failed to get content");
            }
        });
    }

    public void uploadContent(RipeContent ripeContent) {
        // Request Data
        RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.title);

        RequestBody ub = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.uploaded_by);

        RequestBody tags = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.splitTags());

        RequestBody isVideo = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.is_video);

        // Getting file info
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", ripeContent.file.getName(), requestFile);

        Call<ResponseBody> response = ripeService.postContent(isVideo, title, tags, ub, body);
        response.enqueue(new Callback<ResponseBody> () {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("Upload response: ", response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Upload failure: ", "failed to post content");
            }
        });

    }

    public static String createBlobURL(String uuid) {
        return "https://ripeblob2.blob.core.windows.net/usercontent/"+uuid;
    }

}
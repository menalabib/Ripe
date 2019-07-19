package Ripe;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RipeContentService {
    private final Retrofit retrofit;
    private final RipeService ripeService;

    RipeContentService(URL url) {
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
                @Part MultipartBody.Part file
        );

        @GET("get_content_for_user/{uuid}")
        Call<ResponseBody> getContentForUser(
                @Path("uuid") String uuid
        );

        @Multipart
        @PUT("update_content_view/{userId}/{contentId}")
        Call<ResponseBody> updateContentViews(
                @Path("userId") String userId,
                @Path("contentId") String contentId,
                @Part("action") RequestBody action
        );
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

    public List<String> getContentForUser(RipeUser user) {
        Call<ResponseBody> response = ripeService.getContentForUser(user.uuid);
        Type listType = new TypeToken<ArrayList<String>>(){}.getType();
        List<String> list = new ArrayList<>();
        response.enqueue(new Callback<ResponseBody> () {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.print(response.toString());
                if(response.code() == 400) return;
                try {
                    List<String> gson = new Gson().fromJson(response.body().string(), listType);
                    for(String s: gson) {
                        list.add(s);
                    }
                } catch (Exception e) {
                    System.out.println("No content return");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.print("failed to get content");
            }
        });
        return list;
    }

    public void uploadContent(Ripe.RipeContent ripeContent) throws Exception {
        // Request Data
        RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.title);

        RequestBody uid = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.uuid);

        RequestBody ub = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.uploaded_by);

        RequestBody uv = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.upvotes.toString());

        RequestBody dv = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.downvotes.toString());

        RequestBody views = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.views.toString());

        RequestBody tags = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.splitTags());

        // Getting file info
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), ripeContent.file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", ripeContent.file.getName(), requestFile);

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

    public static String createBlobURL(String uuid) {
        return "https://ripeblob2.blob.core.windows.net/usercontent/"+uuid;
    }

}
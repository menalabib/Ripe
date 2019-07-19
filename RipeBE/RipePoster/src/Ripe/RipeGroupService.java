package Ripe;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.*;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RipeGroupService {
    private final Retrofit retrofit;
    private final RipeGroupService.RipeService ripeService;

    RipeGroupService(URL url) {
        this.retrofit = new Retrofit
                .Builder()
                .baseUrl(url)
                .build();

        this.ripeService = retrofit.create(RipeGroupService.RipeService.class);
    }

    private interface RipeService {
        @POST("create_group/{userId}")
        Call<ResponseBody> createGroup (
            @Path("userId") String userId
        );

        @PUT("join_group/{userId}/{groupId}")
        Call<ResponseBody> joinGroup(
                @Path("userId") String userId,
                @Path("groupId") String groupId
        );

        @Multipart
        @POST("post_to_group/{group_id}")
        Call <ResponseBody> postToGroup(
            @Part MultipartBody.Part file,
            @Path("group_id") String group_id
        );

        @GET("get_group_content/{userId}/{groupId}")
        Call<ResponseBody> getGroupContent(
                @Path("userId") String userId,
                @Path("groupId") String groupId
        );

        @Multipart
        @PUT("update_group_content_views/{userID}/{contentID}")
        Call<ResponseBody> updateGroupContent(
            @Path("userID") String userID,
            @Path("contentID") String contentID,
            @Part("action") RequestBody action
        );

        @GET("get_gallery/{groupId}")
        Call<ResponseBody> getGallery(
                @Path("groupId") String groupId
        );
    }

    public void getGallery(String groupId) {
        Type listType = new TypeToken<ArrayList<String>>(){}.getType();
        List<String> list = new ArrayList<>();
        Call<ResponseBody> response = ripeService.getGallery(groupId);
        response.enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            System.out.println("got response");
                        }
                        try {
                            List<String> gson = new Gson().fromJson(response.body().string(), listType);
                            for(String s: gson) {
                                list.add(s);
                            }
                            System.out.println("group gallery found");
                        } catch (Exception e) {
                            System.out.println("group gallery not found");
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        System.out.println("Didn't get response");
                    }
                }
        );
    }

    public void updateGroupContentView(String userId, String contentId, Integer vote) {
        RequestBody action = RequestBody.create(MediaType.parse("multipart/form-data"), vote.toString());
        Call<ResponseBody> response = ripeService.updateGroupContent(userId, contentId, action);

        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    System.out.println("updated group content view");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Could not update content view");
            }
        });
    }

    public void getGroupContent(String userId, String groupId) {
        Call<ResponseBody> response = ripeService.getGroupContent(userId, groupId);
        Type listType = new TypeToken<ArrayList<String>>(){}.getType();
        List<String> list = new ArrayList<>();
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("gotGroupContent");
                try {
                    List<String> gson = new Gson().fromJson(response.body().string(), listType);
                    for(String s: gson) {
                        list.add(s);
                    }
                } catch(Exception exception) {
                    System.out.println("could not find content ids");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Could not get group content");
            }
        });
    }



    public void postToGroup(String groupId, File file) {
        // Getting file info
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Call<ResponseBody> response = ripeService.postToGroup(body, groupId);
        response.enqueue(new Callback<ResponseBody> () {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.print(response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.print("failed to post to group");
            }
        });

    }

    public void joinGroup(String uid, String gid) {
        Call<ResponseBody> response = ripeService.joinGroup(uid,gid);
        response.enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            System.out.println("joined group");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        System.out.print("failed to join group");
                    }
                }
        );
    }

    public void createGroup(String uid) {
        Call<ResponseBody> response = ripeService.createGroup(uid);
        response.enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            System.out.println("created group");
                            try {
                                System.out.println("groupId: "+response.body().string());
                            } catch (Exception e) {
                                System.out.println("But userId not found");
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        System.out.print("failed to create group");
                    }
                }
        );

    }


}

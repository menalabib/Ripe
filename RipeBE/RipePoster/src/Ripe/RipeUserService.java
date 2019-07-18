package Ripe;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.http.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RipeUserService {

    private final Retrofit retrofit;
    private final RipeService ripeService;

    RipeUserService(URL url) {
        this.retrofit = new Retrofit
                .Builder()
                .baseUrl(url)
                .build();

        this.ripeService = retrofit.create(RipeService.class);
    }

    private interface RipeService {
        @Multipart
        @POST("create_user")
        Call<ResponseBody> createUser (
                @Part("name") RequestBody name,
                @Part("email") RequestBody email
        );

        @GET("get_user_by_id/{uuid}")
        Call<ResponseBody> getUserById (
                @Path("uuid") String uuid
        );

        @GET("get_leaderboard")
        Call<ResponseBody> getLeaderboard ();
    }

    public void createUser(RipeUser ripeUser) {
        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), ripeUser.name);

        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), ripeUser.email);

        Call<ResponseBody> responseBodyCall = ripeService.createUser(name, email);
        responseBodyCall.enqueue(new Callback<ResponseBody> () {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ripeUser.uuid = response.body().string().replace("\"", "");
                    System.out.println(ripeUser.uuid);
                } catch(Exception e) {
                    System.out.print("no uuid found");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.print("failed to make user");
            }
        });
    }

    public RipeUser getUserById(String uuid) {
        Type ripeUserType = new TypeToken<RipeUser>(){}.getType();
        RipeUser ripeUser = new RipeUser();

        Call<ResponseBody> responseBodyCall = ripeService.getUserById(uuid);
        responseBodyCall.enqueue(new Callback<ResponseBody> () {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    RipeUser ru = new Gson().fromJson(response.body().string(), ripeUserType);
                    ripeUser.copy(ru);
                } catch(Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.print("failed to get user");
            }
        });
        return ripeUser;
    }

    public void getLeaderboard() {
        Type leaderboardType = new TypeToken<Map<String, List<String>>>(){}.getType();


        Call<ResponseBody> responseBodyCall = ripeService.getLeaderboard();
        responseBodyCall.enqueue(new Callback<ResponseBody> () {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Map<String, List<String>> leaders = new Gson().fromJson(response.body().string(), leaderboardType);
                    System.out.println("got leaderboard");
                } catch(Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.print("failed to get leaderboard");
            }
        });
    }

}
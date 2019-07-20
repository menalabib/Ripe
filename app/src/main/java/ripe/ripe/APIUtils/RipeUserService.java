package ripe.ripe.APIUtils;

import android.content.Context;
import android.content.SharedPreferences;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RipeUserService {

    private Retrofit retrofit;
    private RipeService ripeService;
    private final String url = "http://172.20.10.4:5000/";

    public RipeUserService() {
        try {
            this.retrofit = new Retrofit
                    .Builder()
                    .baseUrl(new URL(url))
                    .build();
            this.ripeService = retrofit.create(RipeService.class);
        }
        catch (MalformedURLException e) { }
    }

    public interface CreateUserCallback {
        void startNav();
    }

    public interface LeaderboardCallback {
        void populateLeaderboard(List<List<String>> leaderboard);
    }

    public interface GetUserCallback {
        void populateUser(RipeUser ripeUsers);
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

    public void createUser(final RipeUser ripeUser, final Context context, final CreateUserCallback ripeCallback) {
        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), ripeUser.name);

        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), ripeUser.email);

        Call<ResponseBody> responseBodyCall = ripeService.createUser(name, email);
        responseBodyCall.enqueue(new Callback<ResponseBody> () {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ripeUser.setUuid(response.body().string().replace("\"", "").replace("\n",""));
                    System.out.println("Creating user: " + ripeUser.uuid);
                    Preference.setSharedPreferenceString(context, "userId", ripeUser.uuid);
                    ripeCallback.startNav();
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

    public void getUserById(String uuid, final GetUserCallback  getUserCallback) {
        final Type ripeUserType = new TypeToken<RipeUser>(){}.getType();

        Call<ResponseBody> responseBodyCall = ripeService.getUserById(uuid);
        responseBodyCall.enqueue(new Callback<ResponseBody> () {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    RipeUser ru = new Gson().fromJson(response.body().string(), ripeUserType);
                    getUserCallback.populateUser(ru);
                } catch(Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.print("failed to get user");
            }
        });
    }

    public void getLeaderboard(final LeaderboardCallback leaderboardCallback) {
        final Type leaderboardType = new TypeToken<List<List<String>>>(){}.getType();
        final List<List<String>> leaderBoard = new ArrayList<>();

        Call<ResponseBody> responseBodyCall = ripeService.getLeaderboard();
        responseBodyCall.enqueue(new Callback<ResponseBody> () {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    List<List<String>> leaders = new Gson().fromJson(response.body().string(), leaderboardType);
                    for(List<String> s : leaders) {
                        leaderBoard.add(s);
                    }
                    System.out.println("got leaderboard");
                    leaderboardCallback.populateLeaderboard(leaderBoard);
                } catch(Exception e) {
                    System.out.println("failed to find leaderboard in responsebody");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.print("failed to get leaderboard");
            }
        });
    }

}
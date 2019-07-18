package Ripe;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;

import java.net.URL;

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
        Call<String> createUser (
                @Part("name") RequestBody name,
                @Part("email") RequestBody email
        );
    }

    public void createUser(RipeUser ripeUser) {
        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), ripeUser.name);

        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), ripeUser.email);

        Call<String> responseBodyCall = ripeService.createUser(name, email);
        responseBodyCall.enqueue(new Callback<String> () {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.print(response.toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.print("failed to get leaderboard");
            }
        });
    }

}
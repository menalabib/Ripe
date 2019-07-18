package Ripe;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
        Call<ResponseBody> createUser (
                @Part("name") RequestBody name,
                @Part("email") RequestBody email
        );
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

}
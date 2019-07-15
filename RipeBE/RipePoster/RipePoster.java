package Ripe;

import Ripe.RipeImage;

public class RipePoster {

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
//                @Body ContentPost p,
                @Part MultipartBody.Part image
        );
    }

    public void uploadImage(RipeImage ripeImage) throws Exception {
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
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        service.postContent(title, uid, tags, ub, uv, dv, views, body).execute();
    }

    public void uploadGif(RipeVideo ripeVideo) throws Exception {
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
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        service.postContent(title, uid, tags, ub, uv, dv, views, body).execute();
    }

}
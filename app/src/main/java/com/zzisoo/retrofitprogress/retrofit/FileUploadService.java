package com.zzisoo.retrofitprogress.retrofit;

import retrofit.Call;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

public interface FileUploadService {
    @Multipart
    @POST("/convert")
    Call<String> upload(
            @Part("myfile\"; filename=\"image.png\" ") ProgressRequestBody file,
            @Part("description") String description);
}
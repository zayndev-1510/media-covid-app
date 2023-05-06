package com.it015.mediacovidapp.apiconfig.admin;

import com.it015.mediacovidapp.model.VideoModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface VideoApi {
    @Multipart
    @POST("app/upload-video")
    Call<ResponseBody> uploadVideo(
            @Part MultipartBody.Part image,
            @Part("judul_video") RequestBody judul,
            @Part("id_kategori") RequestBody id_kategori);

    @Multipart
    @POST("app/upload-foto")
    Call<ResponseBody> uploadFoto(
            @Part MultipartBody.Part image,
            @Part("id_pengguna") RequestBody id_pengguna);
}

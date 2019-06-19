package com.onezero.launcher.launcher.api;


import retrofit2.Call;
import retrofit2.http.POST;

public interface GetAppTypeListService {
    @POST(".")
    Call<PostResult> getPreAppInfoList();
}

package com.wm.tiktokclone.retrofit;

import com.wm.tiktokclone.model.VideoList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VideoService {

    @GET("?key=41393383-5a8469861e82a79bba688f6e8&category=")
    Call<VideoList> getVideos(
            @Query("category") String category
    );
}

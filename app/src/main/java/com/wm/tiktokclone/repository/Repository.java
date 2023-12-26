package com.wm.tiktokclone.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wm.tiktokclone.model.VideoList;
import com.wm.tiktokclone.others.NetworkUtils;
import com.wm.tiktokclone.retrofit.VideoService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    private VideoService videoService;
    private Context context;


    public Repository(VideoService videoService, Context context) {
        this.videoService = videoService;
        this.context = context;
    }

    private MutableLiveData<VideoList> videoListLiveData = new MutableLiveData<>();
    public LiveData<VideoList> getVideo() {
        return videoListLiveData;
    }


    public void getVideos(String category) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            videoService.getVideos(category).enqueue(new Callback<VideoList>() {
                @Override
                public void onResponse(Call<VideoList> call, Response<VideoList> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        videoListLiveData.postValue(response.body());
                    } else {
                        handleUnsuccessfulResponse(response);
                    }
                }

                @Override
                public void onFailure(Call<VideoList> call, Throwable t) {
                    handleFailure(t);
                }
            });
        } else {
            handleNoNetwork();
        }
    }

    private void handleUnsuccessfulResponse(Response<VideoList> response) {
        Toast.makeText(context, "Some Error Occurred", Toast.LENGTH_SHORT).show();
    }

    private void handleFailure(Throwable t) {
        Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show();
        Log.e("Repository", "Error: " + t.getMessage());
    }

    private void handleNoNetwork() {
        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
    }
}

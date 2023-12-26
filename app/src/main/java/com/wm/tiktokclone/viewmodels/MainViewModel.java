package com.wm.tiktokclone.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.wm.tiktokclone.model.VideoList;
import com.wm.tiktokclone.repository.Repository;

public class MainViewModel extends ViewModel {

    private Repository repository;

    public MainViewModel(Repository repository) {
        this.repository = repository;
    }

    public LiveData<VideoList> getVideoLiveData() {
        return repository.getVideo();
    }

    public void fetchVideos(String category) {
        repository.getVideos(category);
    }
}

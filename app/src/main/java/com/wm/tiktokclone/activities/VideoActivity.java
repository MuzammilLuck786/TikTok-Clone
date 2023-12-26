package com.wm.tiktokclone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wm.tiktokclone.R;
import com.wm.tiktokclone.adapter.VideoAdapter;
import com.wm.tiktokclone.model.Small;
import com.wm.tiktokclone.model.VideoList;
import com.wm.tiktokclone.repository.Repository;
import com.wm.tiktokclone.retrofit.RetrofitHelper;
import com.wm.tiktokclone.retrofit.VideoService;
import com.wm.tiktokclone.viewmodels.MainViewModel;
import com.wm.tiktokclone.viewmodels.MainViewModelFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VideoActivity extends AppCompatActivity {

    VideoAdapter videoAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    MainViewModel mainViewModel;
    TextView tvNature,tvFashion,tvScience,tvEducation,tvFeelings,tvHealth,tvComputer,tvFood,tvSports,tvTravel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        VideoService videoService = RetrofitHelper.getInstance().create(VideoService.class);
        Repository repository = new Repository(videoService, this);
        MainViewModelFactory mainViewModelFactory = new MainViewModelFactory(repository);
        mainViewModel = new ViewModelProvider(this, mainViewModelFactory).get(MainViewModel.class);

        recyclerView = findViewById(R.id.rvVideo);
        progressBar = findViewById(R.id.pbVideo);
        tvNature=findViewById(R.id.tvNature);
        tvFashion=findViewById(R.id.tvFashion);
        tvScience=findViewById(R.id.tvScience);
        tvEducation=findViewById(R.id.tvEducation);
        tvFeelings=findViewById(R.id.tvFellings);
        tvHealth=findViewById(R.id.tvHealth);
        tvComputer=findViewById(R.id.tvComputer);
        tvFood=findViewById(R.id.tvFood);
        tvSports=findViewById(R.id.tvSports);
        tvTravel=findViewById(R.id.tvTravel);


        mainViewModel.fetchVideos(tvNature.toString());
        tvNature.setTextColor(ContextCompat.getColor(this, R.color.white));
        tvNature.setBackgroundColor(ContextCompat.getColor(this, R.color.black));

        View[] categoryViews = new View[]{
                tvNature,
                tvFashion,
                tvScience,
                tvEducation,
                tvFeelings,
                tvHealth,
                tvComputer,
                tvFood,
                tvSports,
                tvTravel
        };

        for (View categoryView : categoryViews) {
            if (categoryView != null) {
                categoryView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (View view : categoryViews) {
                            if (view != null) {
                                TextView textView = (TextView) view;
                                if (textView == v) {
                                    textView.setTextColor(Color.WHITE);
                                    textView.setBackgroundColor(Color.BLACK);
                                } else {
                                    textView.setTextColor(Color.BLACK);
                                    textView.setBackgroundColor(Color.WHITE);
                                }
                            }
                        }

                        if (v instanceof TextView) {
                            String selectedCategory = ((TextView) v).getText().toString();
                            mainViewModel.fetchVideos(selectedCategory);
                        }
                    }
                });
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar.setVisibility(View.VISIBLE);

        mainViewModel.getVideoLiveData().observe(this, new Observer<VideoList>() {
            @Override
            public void onChanged(VideoList videoList) {
                if (videoList != null) {
                    videoAdapter = new VideoAdapter(VideoActivity.this,recyclerView, videoList.getHits());
                    recyclerView.setAdapter(videoAdapter);
                    videoAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

                }
            }
        });
    }
}

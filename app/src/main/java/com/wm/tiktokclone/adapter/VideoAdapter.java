package com.wm.tiktokclone.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wm.tiktokclone.R;
import com.wm.tiktokclone.model.Hit;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private Context context;
    private List<Hit> hit;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private int currentPlayingPosition = -1;
    private Handler handler;
    private boolean isScrolling = false;

    public VideoAdapter(Context context, RecyclerView recyclerView, List<Hit> hit) {
        this.context = context;
        this.hit = hit;
        this.recyclerView = recyclerView;

        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert layoutManager != null;
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (firstVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition != RecyclerView.NO_POSITION) {
                    for (int pos = 0; pos < hit.size(); pos++) {
                        if (pos < firstVisibleItemPosition || pos > lastVisibleItemPosition) {
                            ViewHolder viewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(pos);
                            if (viewHolder != null) {
                                viewHolder.pauseVideo();
                            }
                        }
                    }

                    ViewHolder visibleViewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(firstVisibleItemPosition);
                    if (visibleViewHolder != null) {
                        visibleViewHolder.startVideo();
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_video_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(hit.get(position), position);
        if (position == currentPlayingPosition) {
            holder.startVideo();
        } else {
            holder.pauseVideo();
        }
    }

    @Override
    public int getItemCount() {
        return hit.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PlayerView playerView;
        ExoPlayer exoPlayer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.playerView);
            exoPlayer = new ExoPlayer.Builder(context).build();
            playerView.setPlayer(exoPlayer);
        }

        public void bind(Hit hit, int position) {
            MediaItem mediaItem = MediaItem.fromUri(hit.getVideos().getTiny().getUrl());
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
        }

        public void startVideo() {
            exoPlayer.setPlayWhenReady(true);
        }

        public void pauseVideo() {
            exoPlayer.setPlayWhenReady(false);
        }
    }
}
